### Parameterized Test
```java
package function;

import fitnesse.wiki.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;

public class FitnessExample1Test {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

    private static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
                {FitnessExample1.class, true},
                {FitnessExample1.class, false},
                {FitnessExample2.class, true},
                {FitnessExample2.class, false},
                {FitnessExample3.class, true},
                {FitnessExample3.class, false},
                {FitnessExample4.class, true},
                {FitnessExample4.class, false},
        });
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
    public void testableHtml(Class<?> fitnessExampleClass, Boolean includeSuiteSetup) throws Exception {
        String testableHtml = ((Fitness)fitnessExampleClass.newInstance()).testableHtml(pageData, includeSuiteSetup);
//        System.out.printf("testableHtml=[%s]\n", testableHtml);

        setUp();
        String originalTestableHtml = new FitnessExample().testableHtml(pageData, includeSuiteSetup);

        Assertions.assertThat(removeMagicNumber(testableHtml)).isEqualTo(removeMagicNumber(originalTestableHtml));
//        Assertions.assertEquals(removeMagicNumber(originalTestableHtml), removeMagicNumber(testableHtml));
    }
}
```
