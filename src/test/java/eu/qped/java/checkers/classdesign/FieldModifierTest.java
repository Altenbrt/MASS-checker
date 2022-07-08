package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.config.FieldKeywordConfig;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class FieldModifierTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;
    private List<FieldKeywordConfig> fieldKeywordConfigs;
    private FieldKeywordConfig field;


    @DataPoints("accessModifiers")
    public static String[] accessValues() {
        return new String[]{"public", "private", "protected", ""};
    }

    @DataPoints("nonAccessModifiers")
    public static String[] nonAccessValues() {
        return new String[]{
            "static",
            "final",
            "transient",
            "volatile"
        };
    }

    @DataPoint("emptyModifier")
    public static String emptyValue() {
        return "";
    }

    @DataPoints("allNonAccessModifierCombinations")
    public static String[][] allNonAccessValues() {
        String[] possibleNonAccess = nonAccessValues();
        return TestUtils.getAllSubsets(Arrays.asList(possibleNonAccess));
    }



    @DataPoints("choices")
    public static String[] choiceValues() {
        return new String[] {KeywordChoice.YES.toString(), KeywordChoice.NO.toString()};
    }

    @DataPoints("exactMatching")
    public static String[] exactValues() {
        return new String[] {"true", "false"};
    }


    private static void chooseAccessModifier(FieldKeywordConfig field, String accessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("public", () -> field.setPublicModifier(choice));
        runnableMap.put("protected", () -> field.setProtectedModifier(choice));
        runnableMap.put("private", () -> field.setPrivateModifier(choice));
        runnableMap.put("", () -> field.setPackagePrivateModifier(choice));
        runnableMap.get(accessMod).run();
    }

    private static void chooseNonAccessModifier(FieldKeywordConfig field, String nonAccessMod, String choice) {
        Map<String, Runnable> runnableMap = new HashMap<>();
        runnableMap.put("static", () -> field.setStaticModifier(choice));
        runnableMap.put("final", () -> field.setFinalModifier(choice));
        runnableMap.put("transient", () -> field.setTransientModifier(choice));
        runnableMap.put("volatile", () -> field.setVolatileModifier(choice));
        runnableMap.put("", () -> field.setEmptyNonAccessModifier(choice));

        runnableMap.get(nonAccessMod).run();
    }


    //test access modifier
    //test non access modifier
    //test exact and inexact matching
    //test allowing and disallowing variables
    //test empty

    //test exact matching differently?

    //test multiple fields once?


    //test type
    //test name
    //test missing fields
    //test hidden?

    private void init() {
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        fieldKeywordConfigs = new ArrayList<>();
        field = new FieldKeywordConfig();
        ClassKeywordConfig classKeywordConfig = new ClassKeywordConfig();
        classInfo.setClassKeywordConfig(classKeywordConfig);
    }

    private void setup() {
        field.setType("int");
        field.setName("a");
        fieldKeywordConfigs.add(field);
        classInfo.setFieldKeywordConfigs(fieldKeywordConfigs);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);
    }

    @Theory
    public void emptyNonAccessCorrect(@FromDataPoints("accessModifiers") String correctMod,
                                      @FromDataPoints("accessModifiers") String wrongMod,
                                       @FromDataPoints("emptyModifier") String emptyNonAccess,
                                       @FromDataPoints("choices") String choice,
                                       @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();

        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }
        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
                "}";

        setup();
        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void emptyNonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                                    @FromDataPoints("accessModifiers") String wrongMod,
                                    @FromDataPoints("emptyModifier") String emptyNonAccess,
                                    @FromDataPoints("choices") String choice,
                                    @FromDataPoints("exactMatching") String isExactMatch) {
        assumeFalse(correctMod.equals(wrongMod));
        init();
        chooseAccessModifier(field, correctMod, choice);
        chooseNonAccessModifier(field, emptyNonAccess, choice);
        field.setAllowExactModifierMatching(isExactMatch);

        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Collections.singletonList(emptyNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), allowedNonAccess);
        }

        String source = "class TestClass {" + allowedAccess;
        source += " "+String.join(" ", allowedNonAccess);
        source += " int a;"+
                "}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void modifierCorrect(@FromDataPoints("accessModifiers") String correctMod,
                                @FromDataPoints("accessModifiers") String wrongMod,
                                @FromDataPoints("allNonAccessModifierCombinations") String[] nonAccessComb,
                                @FromDataPoints("choices") String choice,
                                @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        init();
        chooseAccessModifier(field, correctMod, choice);
        field.setAllowExactModifierMatching(isExactMatch);


        for (String nonAccess: nonAccessComb) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(nonAccessComb);
        if(choice.equals(KeywordChoice.NO.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), Arrays.asList(nonAccessComb));
        }

        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        source += allowedAccess +" "+String.join(" ", allowedNonAccess) + " int a;}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void accessFault(@FromDataPoints("accessModifiers") String correctMod,
                             @FromDataPoints("accessModifiers") String wrongMod,
                             @FromDataPoints("allNonAccessModifierCombinations") String[] correctNonAccess,
                             @FromDataPoints("choices") String choice,
                             @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));

        init();
        field.setAllowExactModifierMatching(isExactMatch);
        chooseAccessModifier(field, correctMod, choice);
        for (String nonAccess: correctNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }
        String source = "class TestClass {";
        String allowedAccess = correctMod;
        List<String> allowedNonAccess = Arrays.asList(correctNonAccess);
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
        }

        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }

        source += allowedAccess+" "+String.join(" ", allowedNonAccess) + " int a;}";
        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void nonAccessFault(@FromDataPoints("accessModifiers") String accessMod,
                                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                                               @FromDataPoints("choices") String choice,
                                               @FromDataPoints("exactMatching") String isExactMatch) {

        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();
        chooseAccessModifier(field, accessMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }


        source += accessMod+" "+String.join(" ", allowedNonAccess);
        source += " int a;}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }

    @Theory
    public void accessAndNonAccessFault(@FromDataPoints("accessModifiers") String correctMod,
                               @FromDataPoints("accessModifiers") String wrongMod,
                               @FromDataPoints("allNonAccessModifierCombinations") String[] expectedNonAccess,
                               @FromDataPoints("choices") String choice,
                               @FromDataPoints("exactMatching") String isExactMatch) {

        assumeFalse(correctMod.equals(wrongMod));
        List<String> expectedList = Arrays.asList(expectedNonAccess);

        init();
        chooseAccessModifier(field, correctMod, KeywordChoice.YES.toString());
        field.setAllowExactModifierMatching(isExactMatch);
        for (String nonAccess: expectedNonAccess) {
            chooseNonAccessModifier(field, nonAccess, choice);
        }

        String source = "class TestClass {";
        List<String> allowedNonAccess = expectedList;
        String allowedAccess = correctMod;
        if(choice.equals(KeywordChoice.YES.toString())) {
            allowedAccess = wrongMod;
            allowedNonAccess = TestUtils.getDifferenceNonAccess(Arrays.asList(nonAccessValues()), expectedList);
        }
        if(!Boolean.parseBoolean(isExactMatch) && !allowedNonAccess.isEmpty()) {
            allowedNonAccess = Arrays.asList(TestUtils.getAllSubsets(allowedNonAccess)[new Random().nextInt(allowedNonAccess.size())]);
        }


        source += allowedAccess+" "+String.join(" ", allowedNonAccess);
        source += " int a;}";

        setup();

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, classChecker.getClassFeedbacks().size());
    }
}
