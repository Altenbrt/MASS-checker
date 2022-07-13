package eu.qped.java.checkers.mass;

import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.design.DesignChecker;
import eu.qped.java.checkers.design.DesignFeedback;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.feedback.syntax.SyntaxFeedback;

import java.util.ArrayList;
import java.util.List;


public class Mass implements Checker {

    @QfProperty
    private FileInfo file;

    @QfProperty
    private QFMainSettings mainSettings;

    @QfProperty
    private QfMass mass;

    @QfProperty
    private QFClassSettings classSettings;

    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        MainSettings mainSettings = new MainSettings(this.mainSettings);

        System.out.println("reading Main setting");

        // Syntax Checker
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        if (file != null) {
            syntaxChecker.setTargetProject(file.getUnzipped().getPath());
        } else {
            syntaxChecker.setStringAnswer(qfObject.getAnswer());
        }
        // Style Checker

        System.out.println("init style");
        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(mass.getStyle()).build();

        // Semantic Checker

        System.out.println("init semantic");
        SemanticChecker semanticChecker = SemanticChecker.builder().feedbacks(new ArrayList<>()).qfSemSettings(mass.getSemantic()).build();

        System.out.println("init designChecker");
        // Design Checker
        DesignChecker designChecker = DesignChecker.builder().qfDesignSettings(mass.getMetrics()).build();

        //Class Checker
        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(this.classSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);

        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, designChecker, classChecker, mainSettings);
        massExecutor.execute();

        /*
         feedbacks
         */
        List<StyleFeedback> styleFeedbacks;
        styleFeedbacks = massExecutor.getStyleFeedbacks();

        List<SyntaxFeedback> syntaxFeedbacks;
        syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();

        List<SemanticFeedback> semanticFeedbacks;
        semanticFeedbacks = massExecutor.getSemanticFeedbacks();

        List<DesignFeedback> designFeedbacks;
        designFeedbacks = massExecutor.getDesignFeedbacks();

        List<ClassFeedback> classFeedbacks;
        classFeedbacks = massExecutor.getClassFeedbacks();

        System.out.println(">>>>>>>>>>>>>>>>>44444444444");

        System.out.println(styleFeedbacks.size());
        System.out.println(semanticFeedbacks.size());
        System.out.println(designFeedbacks.size());
//        System.out.println(classFeedbacks.size());
        System.out.println(syntaxFeedbacks.size());

        System.out.println(">>>>>>>>>>>>>>>>>44444444444");

//        String[] result = new String[styleFeedbacks.size() + semanticFeedbacks.size() + designFeedbacks.size() + classFeedbacks.size() + syntaxFeedbacks.size() + 100];
        String[] result = new String[styleFeedbacks.size() + semanticFeedbacks.size() + designFeedbacks.size()  + syntaxFeedbacks.size() + 100];

        int i = 0;

        for (StyleFeedback styleFeedback : styleFeedbacks) {
            result[i] = "style Feedback";
            result[i + 1] =
                    styleFeedback.getFile()
                            + NEW_LINE
                            + styleFeedback.getDesc()
                            + NEW_LINE
                            + styleFeedback.getContent()
                            + NEW_LINE
                            + styleFeedback.getLine()
                            + NEW_LINE
                            + styleFeedback.getExample()
                            + NEW_LINE;
            i = i + 2;
        }
        System.out.println(">>>>>>>>>>>>>>>>>333333333333333333");
        for (SemanticFeedback semanticFeedback : semanticFeedbacks) {
            result[i] = "semantic Feedback";
            result[i + 1] = semanticFeedback.getBody() + NEW_LINE
                    + "--------------------------------------------------";
            i = i + 2;
        }
        System.out.println(">>>>>>>>>>>>>>>>>333333333333333333");
        for (DesignFeedback df : designFeedbacks) {
            result[i] = "design Feedback";
            result[i + 1] =
                    "In class '" + df.getClassName() + ".java'"
                            + NEW_LINE
                            + df.getMetric() + " (" + df.getBody() + ")"
                            + NEW_LINE
                            + df.getMetric() + " (" + df.getBody() + ")"
                            + NEW_LINE
                            + "Measured with value: " + df.getValue()
                            + NEW_LINE
                            + df.getSuggestion()
                            + "------------------------------------------------------------------------------";
            i = i + 2;
        }

        for (ClassFeedback classFeedback : classFeedbacks) {
            result[i] = "class Feedback";
            result[i + 1] = classFeedback.getBody() + NEW_LINE
                    + "--------------------------------------------------";
            i = i + 2;
        }

        for (SyntaxFeedback syntax : syntaxFeedbacks) {
            result[i + 1] = ""
                    + syntax.toString()
                    + NEW_LINE
                    + "--------------------------------------------------";
            i = i + 2;
        }

        qfObject.setFeedback(result);
    }

}