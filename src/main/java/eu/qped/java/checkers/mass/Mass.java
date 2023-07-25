package eu.qped.java.checkers.mass;

import eu.qped.framework.*;
import eu.qped.framework.QpedQfFilesUtility.CreatedAnswerFileSummary;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.mutation.MutationChecker;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;


public class Mass implements Checker {

    public static final String SEPARATOR = "--------------------------------------------------";

    @QfProperty
    private FileInfo file;

    @QfProperty
    private QfMass mass;

    @QfProperty
    private QfClassSettings classSettings;

    private final static String NEW_LINE = "\n" + "\n";

    @Getter
    @AllArgsConstructor
    public static class SolutionWorkspace {
        private File solutionDirectory;
        private List<File> studentResources;
        private List<File> instructorResources;
        private Map<String, Integer> filenameToLineOffset;
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        MassExecutor.MassExecutorBuilder massExecutorBuilder = MassExecutor.builder();
        String preferredLanguage = "en";
        
        if (qfObject.getUser() != null) {
            preferredLanguage =  qfObject.getUser().getLanguage();
        } 
        MainSettings mainSettings = new MainSettings(mass, preferredLanguage);
        massExecutorBuilder.mainSettings(mainSettings);

        Map<String, Integer> filenameToLineOffset = new HashMap<>();

        // get student solution
        // if necessary unpack a zip file or create a java file if only a String answer is provided
        // also create a list of all student resources and for String answers record the generated file name and possible line offset within the class
        File solutionRoot;
        if (file != null) {
            solutionRoot = QpedQfFilesUtility.downloadAndUnzipIfNecessary(file);
            var allJavaFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "java");
            if (allJavaFiles.isEmpty()) {
                throw new IllegalArgumentException("Uploaded solution does not contain files with extension java (either as single file or containted in zip archive.");
            }
        } else {
            solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
            CreatedAnswerFileSummary summary = QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, qfObject.getAnswer());
            filenameToLineOffset.put(summary.getFileName(), summary.getLineOffset());
        }

        List<File> studentResources = QpedQfFilesUtility.filesWithExtension(solutionRoot, null);

        // if necessary, get instructor resources
        String instructorResourcesURL = mass.getInstructorResources();
        File instructorRoot = null;
        List<File> instructorResources = Collections.emptyList();

        // is there a URL for the instructor's resources provided?
        if (instructorResourcesURL != null && !instructorResourcesURL.isBlank()) {

            // create file info for the provided URL
            FileInfo instructorResourcesFileInfo = null;

            // But first check if the URL has the protocol "qf" such as in "qf:provided.zip"
            String[] splitUrl = instructorResourcesURL.split(":");
            if (splitUrl.length >= 2 && splitUrl[0].equalsIgnoreCase("qf")) {
                // in this case, and if the URL is well formed,
                // we search if there is a corresponding file info in the QF object
                String qfFile = splitUrl[1];
                int extensionStart = qfFile.lastIndexOf('.');
                String qfFileLabel = qfFile.substring(0, extensionStart);
                String qfFileExtension = qfFile.substring(extensionStart);
                for (FileInfo fileInfo : qfObject.getAssignment().getFiles()) {
                    if (fileInfo.getLabel().equals(qfFileLabel) && fileInfo.getExtension().equals(qfFileExtension)) {
                        // if we find a file info, use this file info instead
                        instructorResourcesFileInfo = fileInfo;
                        break;
                    }
                }
                if (instructorResourcesFileInfo == null) {
                    throw new RuntimeException("Provided URL for instructor resources illegal. "
                            + "Used protocol 'qf' but no corresponding file info defined in QF-object, i.e., "
                            + "no such file uploaded in Quarterfall question. (" + instructorResourcesURL + ")");
                }
            } else {
                instructorResourcesFileInfo = FileInfo.createForUrl(instructorResourcesURL);
            }
            instructorRoot = QpedQfFilesUtility.downloadAndUnzipIfNecessary(instructorResourcesFileInfo);
            instructorResources = QpedQfFilesUtility.filesWithExtension(instructorRoot, null);
            FileUtils.copyDirectory(instructorRoot, solutionRoot);
        }

        SolutionWorkspace solutionWorkspace = new SolutionWorkspace(solutionRoot, studentResources, instructorResources, filenameToLineOffset);

        // Syntax Checker
        // configuration for syntax check is always needed
        var syntaxSetting = SyntaxSetting.builder()
                .language(
                        qfObject.getUser() != null ?
                                qfObject.getUser().getLanguage() :
                                Locale.ENGLISH.getLanguage()
                )
                .checkLevel(
                        mass.getSyntax() != null && mass.getSyntax().getLevel() != null && EnumSet.allOf(CheckLevel.class).contains(CheckLevel.valueOf(mass.getSyntax().getLevel()))
                                ? CheckLevel.valueOf(mass.getSyntax().getLevel())
                                : CheckLevel.BEGINNER
                )
                .build();
        SyntaxChecker syntaxChecker = SyntaxChecker.builder()
                .targetProject(solutionRoot)
                .syntaxSetting(syntaxSetting)
                .build();
        massExecutorBuilder.syntaxChecker(syntaxChecker);

        // Style Checker
        if (mass.isStyleSelected()) {
            StyleChecker styleChecker = StyleChecker.builder().
                    qfStyleSettings(mass.getStyle()).
                    build();
            massExecutorBuilder.styleChecker(styleChecker);
        }

        // Solution Approach Checker
        if (mass.isSemanticSelected()) {
            mass.getSemantic().setLanguage(
                    qfObject.getUser() != null ?
                            qfObject.getUser().getLanguage() :
                            Locale.ENGLISH.getLanguage()
            );
            SolutionApproachChecker solutionApproachChecker = SolutionApproachChecker.builder().
                    qfSemanticSettings(mass.getSemantic()).
                    build();
            massExecutorBuilder.solutionApproachChecker(solutionApproachChecker);
        }

        // Metrics Checker
        if (mass.isMetricsSelected()) {
            MetricsChecker metricsChecker = MetricsChecker.builder().
                    qfMetricsSettings(mass.getMetrics()).
                    solutionRoot(solutionRoot).
                    build();
            massExecutorBuilder.metricsChecker(metricsChecker);
        }

        // Class Checker
        if (mass.isClassSelected()) {
            ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(mass.getClasses());
            ClassChecker classChecker = new ClassChecker(classConfigurator);
            massExecutorBuilder.classChecker(classChecker);
        }

        // Coverage Checker
        if (mass.isCoverageSelected()) {
            CoverageChecker coverageChecker = new CoverageChecker(mass.getCoverage(), solutionRoot);
            massExecutorBuilder.coverageChecker(coverageChecker);
        }

        // Coverage Checker
        if (mass.isMutationSelected()) {
            MutationChecker mutationChecker = new MutationChecker(mass.getMutation(), solutionRoot);
            massExecutorBuilder.mutationChecker(mutationChecker);
        }

        //Mass
        MassExecutor massExecutor = massExecutorBuilder.build();
        massExecutor.execute();

        
        /*
         feedbacks
         */
        var syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();
        var styleFeedbacks = massExecutor.getStyleFeedbacks();
        var solutionApproachFeedbacks = massExecutor.getSolutionApproachFeedbacks();
        var metricsFeedbacks = massExecutor.getMetricsFeedbacks();
        var coverageFeedbacks = massExecutor.getCoverageFeedbacks();
        var mutationFeedbacks = massExecutor.getMutationFeedbacks();

        var resultArray = mergeFeedbacks(
                syntaxFeedbacks,
                styleFeedbacks,
                solutionApproachFeedbacks,
                metricsFeedbacks,
                coverageFeedbacks,
                mutationFeedbacks,
                qfObject,
                solutionRoot
        );

        qfObject.setFeedback(resultArray);
    }

    private String[] mergeFeedbacks(
            @NonNull List<String> syntaxFeedbacks,
            @NonNull List<String> styleFeedbacks,
            @NonNull List<String> semanticFeedbacks,
            @NonNull List<MetricsFeedback> metricsFeedbacks,
            @NonNull List<String> coverageFeedbacks,
            @NonNull List<String> mutationFeedbacks,
            @NonNull QfObject qfObject, File solutionRoot
    ) {

        var resultSize =
                !syntaxFeedbacks.isEmpty() ? syntaxFeedbacks.size() + 1 :
                        styleFeedbacks.size() + semanticFeedbacks.size() + metricsFeedbacks.size() + 3;

        String[] resultArray = new String[resultSize];
        List<String> resultArrayAsList = new ArrayList<>();
        resultArrayAsList.add("# Your Feedback\n");
        if (!syntaxFeedbacks.isEmpty()) {
            resultArrayAsList.addAll(syntaxFeedbacks);
        } else {
            if (!styleFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Style feedbacks\n");
            }
            resultArrayAsList.addAll(styleFeedbacks);
            if (!semanticFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Semantic feedbacks");
            }
            resultArrayAsList.addAll(semanticFeedbacks);
            if (!metricsFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Metric feedbacks");
            }
            metricsFeedbacks.forEach(
                    metricsFeedback -> {
                        String tempFeedbackAsString =
                                MarkdownFormatterUtility.asHeading4("In class " + MarkdownFormatterUtility.asCodeLine(metricsFeedback.getClassName() + ".java"))
                                        + MarkdownFormatterUtility.asBold(metricsFeedback.getMetric() + " (" + metricsFeedback.getBody() + ")")
                                        + " measured with value: " + MarkdownFormatterUtility.asCodeLine(Double.toString(metricsFeedback.getValue()))
                                        + NEW_LINE
                                        + metricsFeedback.getSuggestion()
                                        + NEW_LINE
                                        + SEPARATOR;
                        resultArrayAsList.add(tempFeedbackAsString);
                    }
            );
            if (!coverageFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Coverage feedbacks");
            }
            resultArrayAsList.addAll(coverageFeedbacks);

            if (!mutationFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Mutation feedbacks");
            }
            resultArrayAsList.addAll(mutationFeedbacks);

        }
        if (resultArrayAsList.size() <= 1) {
            resultArrayAsList.add("Our checks could not find any improvements for your code. This does not mean that it is semantically correct but it adheres to the standards of the lecture in regards to syntax and style.");
        }

        String solutionRootName = solutionRoot.getAbsolutePath() + File.separator;
        return resultArrayAsList.stream()
                .map(s -> s.replace(solutionRootName, ""))
                .toArray(size -> new String[size]);
    }

}