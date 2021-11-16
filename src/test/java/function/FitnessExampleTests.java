package function;

import fitnesse.wiki.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class FitnessExampleTests {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

    private static Collection<Object[]> parameters() {
        return Arrays.stream(new Object[]{
                FitnessExample1.class,
                FitnessExample2.class,
                FitnessExample3.class,
                FitnessExample4.class,
                FitnessExample5.class,
                FitnessExample6.class,
                FitnessExample7.class,
                FitnessExample8.class,
        }).flatMap(clazz -> Arrays.stream(new Object[][]{{clazz, true}, {clazz, false}})).collect(Collectors.toList());
    }

    @BeforeEach
    public void setUp() throws Exception {
        root = InMemoryPage.makeRoot("RooT");
        crawler = root.getPageCrawler();
        testPage = addPage("TestPage", "!define TEST_SYSTEM {slim}\n" + "the content");
        addPage("SetUp", "setup");
        addPage("TearDown", "teardown");
        addPage("SuiteSetUp", "suiteSetUp");
        addPage("SuiteTearDown", "suiteTearDown");

        crawler.addPage(testPage, PathParser.parse("ScenarioLibrary"), "scenario library 2");

        pageData = testPage.getData();
    }

    private WikiPage addPage(String pageName, String content) throws Exception {
        return crawler.addPage(root, PathParser.parse(pageName), content);
    }

    private String removeMagicNumber(String expectedResult) {
        return expectedResult.replaceAll("[-]*\\d+", "");
    }

    @ParameterizedTest
    @MethodSource(value = "parameters")
    public void testableHtml(Class<? extends Fitness> fitnessExampleClass, Boolean includeSuiteSetup) throws Exception {
        String testableHtml = fitnessExampleClass.newInstance().testableHtml(pageData, includeSuiteSetup);
//        System.out.printf("testableHtml=[%s]\n", testableHtml);

        setUp();
        String originalTestableHtml = new FitnessExample5().testableHtml(pageData, includeSuiteSetup);

        Assertions.assertThat(removeMagicNumber(testableHtml)).isEqualTo(removeMagicNumber(originalTestableHtml));
//        Assertions.assertEquals(removeMagicNumber(originalTestableHtml), removeMagicNumber(testableHtml));
    }
}
