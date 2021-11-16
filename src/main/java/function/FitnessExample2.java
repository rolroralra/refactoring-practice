package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample2 implements Fitness {
    static StringBuffer buffer = new StringBuffer();

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        PageData tmpPageData = pageData;

        testPage(SuiteResponder.SUITE_SETUP_NAME, pageData, includeSuiteSetup);

        buffer.append(pageData.getContent());

        testPage(SuiteResponder.SUITE_TEARDOWN_NAME, pageData, includeSuiteSetup);

        tmpPageData.setContent(buffer.toString());
        return tmpPageData.getHtml();
    }

    private void testPage(String suiteTypeName, PageData pageData, boolean includeSuiteSetup) throws Exception {
        if (pageData.hasAttribute("Test")) {
            recodePagePath(replaceSuiteTypeName(suiteTypeName), pageData.getWikiPage());

            if (includeSuiteSetup) {
                renderPagePath(suiteTypeName, pageData.getWikiPage());
            }
        }
    }

    private void recodePagePath(String step, WikiPage wikiPage) throws Exception {
        WikiPage inheritedWikiPage = PageCrawlerImpl.getInheritedPage(step, wikiPage);
        if (inheritedWikiPage != null) {
            buffer.append("!include -" + step + " .")
                    .append(PathParser.render(wikiPage.getPageCrawler().getFullPath(inheritedWikiPage))).append("\n");
        }
    }

    private void renderPagePath(String suite, WikiPage wikiPage) throws Exception {
        WikiPage suitePage = PageCrawlerImpl.getInheritedPage(suite, wikiPage);
        if (suitePage != null) {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suitePage);
            String step = suite.equals(SuiteResponder.SUITE_TEARDOWN_NAME) ? "teardown" : "setup";
            buffer.append("!include -" + step + " .").append(PathParser.render(pagePath)).append("\n");
        }
    }

    private String replaceSuiteTypeName(String suiteType) {
        return suiteType.substring(5);
    }
}

