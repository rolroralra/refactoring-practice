package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample4 implements Fitness {
    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        String pageContent = pageData.getContent();
        if (!isTestPage(pageData)) {
            return pageContent;
        }

        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        if (includeSuiteSetup) {
            addTestAttribute(wikiPage, buffer, SuiteResponder.SUITE_SETUP_NAME,
                    TestableHTMLConstant.INCLUDE_SETUP);
        }

        addTestAttribute(wikiPage, buffer, TestableHTMLConstant.SETUP,
                    TestableHTMLConstant.INCLUDE_SETUP);

        buffer.append(pageContent);

        addTestAttribute(wikiPage, buffer, TestableHTMLConstant.TEARDOWN,
                    TestableHTMLConstant.INCLUDE_TEARDOWN);

        if (includeSuiteSetup) {
            addTestAttribute(wikiPage, buffer, SuiteResponder.SUITE_TEARDOWN_NAME,
                    TestableHTMLConstant.INCLUDE_TEARDOWN);
        }

        PageData returnPageData = new PageData(pageData);
        returnPageData.setContent(buffer.toString());
        return returnPageData.getHtml();
    }


    private boolean isTestPage(PageData pageData) throws Exception {
        return pageData.hasAttribute("Test");
    }


    private void addTestAttribute(WikiPage wikiPage, StringBuffer buffer,
                                  String testAttribute, String appendAttribute)
            throws Exception {
        WikiPage testPage = PageCrawlerImpl.getInheritedPage(testAttribute, wikiPage);

        if (testPage == null) {
            return;
        }

        WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(testPage);
        String pagePathName = PathParser.render(pagePath);
        buffer.append(appendAttribute).append(pagePathName).append("\n");
    }

    public final class TestableHTMLConstant {

        private TestableHTMLConstant() {
            throw new AssertionError("Could not instantiate Constant class.");
        }

        public static final String SETUP = "SetUp";
        public static final String TEARDOWN = "TearDown";
        public static final String INCLUDE_SETUP = "!include -setup .";
        public static final String INCLUDE_TEARDOWN = "!include -teardown .";
    }

}

