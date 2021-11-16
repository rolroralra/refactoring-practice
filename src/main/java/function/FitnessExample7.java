package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample7 implements Fitness {
    private static final String INCLUDE_SETUP = "!include -setup .";
	private static final String INCLUDE_TEARDOWN = "!include -teardown .";

	public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        boolean hasAttribute = pageData.hasAttribute("Test");
		if (hasAttribute) { 
            setUp(includeSuiteSetup, wikiPage, buffer);
            
            buffer.append(pageData.getContent());
            
            tearDown(includeSuiteSetup, wikiPage, buffer);
        } else {
            buffer.append(pageData.getContent());
        }
		
        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

	private void tearDown(boolean includeSuiteSetup, WikiPage wikiPage, StringBuffer buffer) throws Exception {
		setUpPage(wikiPage, buffer, "TearDown", INCLUDE_TEARDOWN);

		if (includeSuiteSetup) { 
		    setUpPage(wikiPage, buffer, SuiteResponder.SUITE_TEARDOWN_NAME, INCLUDE_TEARDOWN);
		}
	}

	private void setUp(boolean includeSuiteSetup, WikiPage wikiPage, StringBuffer buffer) throws Exception {
		if (includeSuiteSetup) {
		    setUpPage(wikiPage, buffer, SuiteResponder.SUITE_SETUP_NAME, INCLUDE_SETUP);
		}
		setUpPage(wikiPage, buffer, "SetUp", INCLUDE_SETUP);
	} 

    private void setUpPage(WikiPage wikiPage, StringBuffer buffer, String pageName, String pageTest) throws Exception {
        WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (suiteSetup != null) {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append(pageTest).append(pagePathName).append("\n");
        }
    }
}
