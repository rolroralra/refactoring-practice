package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample1 implements Fitness {
    private static final String TEST_ATTRIBUTE = "Test";
    private static final String INCLUDE_SETUP = "!include -setup .";
    private static final String INCLUDE_TEARDOWN = "!include -teardown .";
    private static final String SET_UP = "SetUp";
    private static final String TEAR_DOWN = "TearDown";
    private static final String NEW_LINE = "\n";

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        final boolean hasTestAttribute = hasTestAttribute(wikiPage);

        if (hasTestAttribute) {
            setUpWikiPage(includeSuiteSetup, wikiPage, buffer);
        }

        buffer.append(pageData.getContent());

        if (hasTestAttribute) {
            tearDownWikiPage(includeSuiteSetup, wikiPage, buffer);
        }

        // Result
        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

    private void setUpWikiPage(boolean includeSuite, WikiPage wikiPage, StringBuffer buffer) throws Exception {
        WikiPage setUpFullPath = PageCrawlerImpl.getInheritedPage(SET_UP, wikiPage);

        if (includeSuite) {
            WikiPage setUpSuiteFullPath = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
            crawlerSetup(wikiPage, buffer, setUpSuiteFullPath);
        }

        crawlerSetup(wikiPage, buffer, setUpFullPath);
    }

    private void tearDownWikiPage(boolean includeSuite, WikiPage wikiPage, StringBuffer buffer) throws Exception {
        WikiPage tearDownFullPath = PageCrawlerImpl.getInheritedPage(TEAR_DOWN, wikiPage);

        crawlerTeardown(wikiPage, buffer, tearDownFullPath);

        if (includeSuite) {
            WikiPage tearDownSuiteFullPath = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
            crawlerTeardown(wikiPage, buffer, tearDownSuiteFullPath);
        }
    }

    private void crawlerTeardown(WikiPage wikiPage, StringBuffer buffer, WikiPage tearDownFullPath)
            throws Exception {
        crawler(wikiPage, buffer, tearDownFullPath, INCLUDE_TEARDOWN);
    }

    private void crawlerSetup(WikiPage wikiPage, StringBuffer buffer, WikiPage setUpFullPath) throws Exception {
        crawler(wikiPage, buffer, setUpFullPath, INCLUDE_SETUP);
    }

    private void crawler(WikiPage wikiPage, StringBuffer buffer, WikiPage wikiPageFullPath, String description) throws Exception {
        if (wikiPageFullPath == null) {
            return;
        }

        WikiPagePath wikiPagePath = wikiPage.getPageCrawler().getFullPath(wikiPageFullPath);
        String pagePathName = PathParser.render(wikiPagePath);
        buffer.append(description).append(pagePathName).append(NEW_LINE);
    }

    private boolean hasTestAttribute(WikiPage wikiPage) throws Exception {
        return wikiPage.getData().hasAttribute(TEST_ATTRIBUTE);
    }
}
