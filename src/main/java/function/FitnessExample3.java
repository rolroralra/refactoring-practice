package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample3 implements Fitness {
    public static final String TEST_PAGE_ATTRIBUTE = "Test";
    public static final String TEAR_DOWN = "TearDown";
    public static final String SET_UP = "SetUp";
    public static final String INCLUDE_SETUP = "!include -setup .";
    public static final String INCLUDE_TEARDOWN = "!include -teardown .";
    public static final String NEW_LINE = "\n";

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        if (pageData.hasAttribute(TEST_PAGE_ATTRIBUTE)) {
            setUpWikiPage(includeSuiteSetup, wikiPage, buffer);
        }

        buffer.append(pageData.getContent());

        if (pageData.hasAttribute(TEST_PAGE_ATTRIBUTE)) {
            tearDown(includeSuiteSetup, wikiPage, buffer);
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

    private void tearDown(boolean includeSuite, WikiPage wikiPage, StringBuffer buffer) throws Exception {
        WikiPage teardown = PageCrawlerImpl.getInheritedPage(TEAR_DOWN, wikiPage);

        // Tear Down
        crawlingAndAppend(wikiPage, buffer, teardown, INCLUDE_TEARDOWN);

        // Suite Tear Down
        if (includeSuite) {
            WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
            crawlingAndAppend(wikiPage, buffer, suiteTeardown, INCLUDE_TEARDOWN);
        }
    }

    private void setUpWikiPage(boolean includeSuite, WikiPage wikiPage, StringBuffer buffer) throws Exception {
        // SUITE_SETUP
        if (includeSuite) {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
            crawlingAndAppend(wikiPage, buffer, suiteSetup, INCLUDE_SETUP);
        }

        // SETUP
        WikiPage setup = PageCrawlerImpl.getInheritedPage(SET_UP, wikiPage);
        crawlingAndAppend(wikiPage, buffer, setup, INCLUDE_SETUP);
    }

    private void crawlingAndAppend(WikiPage wikiPage, StringBuffer buffer, WikiPage subPage, String description) throws Exception {
        if (subPage == null) {
            return;
        }

        WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(subPage);
        String pagePathName = PathParser.render(pagePath);
        buffer.append(description).append(pagePathName).append(NEW_LINE);
    }
}
