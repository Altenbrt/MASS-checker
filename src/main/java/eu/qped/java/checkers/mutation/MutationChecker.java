package eu.qped.java.checkers.mutation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.coverage.FeedbackMessage;
import eu.qped.java.checkers.coverage.MemoryLoader;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.coverage.framework.coverage.Jacoco;
import eu.qped.java.checkers.coverage.framework.test.JUnit5;
import eu.qped.java.checkers.coverage.framework.test.TestFramework;
import eu.qped.java.checkers.mass.QfCoverageSettings;
import eu.qped.java.checkers.mass.QfMutationSettings;
import eu.qped.java.checkers.mass.ShowFor;
import eu.qped.java.checkers.mutation.interfaces.BasicTemplate;
import eu.qped.java.checkers.mutation.interfaces.StudentTests;
import eu.qped.java.checkers.mutation.interfaces.Versions;

public class MutationChecker {

//	private QfCoverageSettings covSettings;
	
	private File solutionRoot;
	private QfMutationSettings settings;

	public MutationChecker(QfMutationSettings settings, File solutionRoot) {
		this.settings = settings;
		this.solutionRoot = solutionRoot;
	}



	public List<String> check() {
		// Determine the application classes and test classes in the solution
		List<MutationFacade> testClasses = new ArrayList<>();
        List<MutationFacade> classes = new ArrayList<>();
        try {
			separateTestAndApplicationClasses(testClasses, classes);
		} catch (IOException e) {
			throw new RuntimeException("Could not create lists of test and application classes.", e);
		}

        // Create a classloader for the student and instructor classes
		MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
		for (MutationFacade testClass : testClasses) {
			memoryLoader.upload(testClass.className(), testClass.byteCode());
		}
		for (MutationFacade clazz : classes) {
			memoryLoader.upload(clazz.className(), clazz.byteCode());
		}

		// create a runner for the tests, currently only JUnit 5 supported
		TestFramework test = new JUnit5();

		// List of messages (in Markdown) that will be displayed to students.
		// Leave list empty for no feeback.
		List<String> messages = new ArrayList<>();
		
		List<String> testClassNames = testClasses.stream().map(MutationFacade::className).collect(Collectors.toList());

		messages.add("testClassNames: ");
		for (String s : testClassNames) {
			messages.add(s);
		}
		messages.add("Classes: ");
		BasicTemplate bb = new BasicTemplate() {
			@Override
			public Object programm(Object... args) {
				return null;
			}
		};
		for (MutationFacade c: classes) {
			messages.add("#"+c.className());

			if (c.getaClass().isInstance(bb))
				messages.add("It works !!!!!!!!!!!!!!!!!!!!!!!!!!");
			Arrays.stream(c.getaClass().getFields()).forEach(x -> messages.add(x.getName()));
			Arrays.stream(c.getaClass().getConstructors()).forEach(x -> messages.add(x.getName()));
		}

		/*
		//Getting Basic Implementation
		BasicTemplate programm = new GrayCode();

		//Getting Student Test
		StudentTests studentTests = new CrayCodeTest(programm);

		//Getting the Enum
		Versions[] v = GrayCodeEnum.values();


		int testsExecuted = 0;
		long startTime = System.currentTimeMillis();

		for (Versions implementation : v) {
			String message = implementation + ", ";

			//...
			programm.configuration(implementation);
			try{
				//ignore output potentially generated by tests
				//System.setOut(dummyOut);

				studentTests.test();
				//At this point the Test where all accepted
				if (implementation.getExpected())
					message += "Das Programm lief hier wie erwartet duch. ";
				else
					message += "Hier wurde kein Fehler erkannt. Es gibt jedoch einen. " + implementation.getFeedback();
				// enable output again
				//System.setOut(realOut);
			} catch (Error e) {
				// enable output again
				//System.setOut(realOut);

				if (implementation.getExpected())
					message += "Es sollte es keinen Fehler geben, doch es wurde einer erkannt. Der Fehleroutput war:\n" + e.getMessage() +"\n" + implementation.getFeedback();
				else
					message += "Hier wurde ein Fehler erkannt. Genauso sollte es sein. ";
			}


			testsExecuted++;
			messages.add(message);
		}

		/*
		// Loop over variants
		// (replace with proper loop condition
		for (int i = 0; i < 1; i++) {
			// configure selection of appropriate variant
			
			// run all tests
			List<String> testResults = test.testing(testClassNames, memoryLoader);
			// if testResults is empty, all tests succeeded (or there were no tests)
			// depending on the type of variant (is it the correct implementation, or a mutant with a defect)
			// generate appropriate feedback message (i.e., the message configured by the instructor for this case)
			// and add it to 'messages'.
		}
		// end loop

		 */

		messages.add("#TEst42");
		return messages;


	}

	public void separateTestAndApplicationClasses(List<MutationFacade> testClasses, List<MutationFacade> classes)
			throws IOException {
		List<File> allClassFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "class");
        String solutionDirectoryPath = solutionRoot.getCanonicalPath() + File.separator;

        // The file separator will be used as regular expression by replaceAll.
        // Therefore, we must escape the separator on Windows systems.
        String fileSeparator = File.separator;
        if (fileSeparator.equals("\\")) {
        	fileSeparator = "\\\\";
        }
        for (File file : allClassFiles) {
        	String filename = file.getCanonicalPath();
        	Class aClass = file.getClass();

        	String classname = filename.
        			substring(solutionDirectoryPath.length(), filename.length() - ".class".length()).
        			replaceAll(fileSeparator, ".");

        	String[] classnameSegments = classname.split("\\.");
        	String simpleClassname = classnameSegments[classnameSegments.length - 1];
        	
        	MutationFacade mutationFacade = new MutationFacade(
        			Files.readAllBytes(file.toPath()),
        			classname, aClass);
        	
        	if (simpleClassname.startsWith("Test") 
        			|| simpleClassname.startsWith("test")
        			|| simpleClassname.endsWith("Test")
        			|| simpleClassname.endsWith("test")) {
        		// the class is a test
        		testClasses.add(mutationFacade);
        	} else {
        		// the class is an application class (i.e., no test)
        		classes.add(mutationFacade);
        	}
        }
	}

}
