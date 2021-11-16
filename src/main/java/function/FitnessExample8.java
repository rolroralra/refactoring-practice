package function;

import fitnesse.wiki.PageCrawlerImpl;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class FitnessExample8 implements Fitness {
    StringBuffer buffer;

    WikiPage wikiPage;

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        if (isTestMode(pageData)) {
            this.buffer = new StringBuffer();
            this.wikiPage = pageData.getWikiPage();
            appendSetupMessage(includeSuiteSetup);
            appendPageContent(pageData);
            appendTearDownMessage(includeSuiteSetup);
            pageData.setContent(this.buffer.toString());
        }
        return pageData.getHtml();
    }

    private boolean isTestMode(PageData pageData) throws Exception {
        return pageData.hasAttribute("Test");
    }

    private void appendSetupMessage(boolean includeSuiteSetup) throws Exception {
        if (includeSuiteSetup)
            appendNewLine(this.wikiPage, "SuiteSetUp", "!include -setup .");
        appendNewLine(this.wikiPage, "SetUp", "!include -setup .");
    }

    private void appendPageContent(PageData pageData) throws Exception {
        this.buffer.append(pageData.getContent());
    }

    private void appendTearDownMessage(boolean includeSuiteSetup) throws Exception {
        appendNewLine(this.wikiPage, "TearDown", "!include -teardown .");
        if (includeSuiteSetup)
            appendNewLine(this.wikiPage, "SuiteTearDown", "!include -teardown .");
    }

    private void appendNewLine(WikiPage wikiPage, String pageName, String message) throws Exception {
        WikiPage page = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (page != null) {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(page);
            String pathName = PathParser.render(pagePath);
            this.buffer.append(message).append(pathName).append("\n");
        }
    }
}

