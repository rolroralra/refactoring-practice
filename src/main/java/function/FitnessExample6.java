package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.PageCrawlerImpl;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class FitnessExample6 implements Fitness {
	public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
		WikiPage wikiPage = pageData.getWikiPage();
		StringBuffer buffer = new StringBuffer();

		final boolean hasTestAttribute = pageData.hasAttribute("Test");

		if (hasTestAttribute) {
			final String setupTag = "setup";

			String suiteSetupName = SuiteResponder.SUITE_SETUP_NAME;
			includeSuiteSetup(includeSuiteSetup, wikiPage, buffer, setupTag, suiteSetupName);

			final String SETUP_NAME = "SetUp";
			String setupPathName = getPagePathName(wikiPage, SETUP_NAME);
			appendMessage(buffer, setupTag, setupPathName);
		}

		buffer.append(pageData.getContent());

		if (hasTestAttribute) {
			final String teardownTag = "teardown";
			final String TEAR_DOWN_NAME = "TearDown";

			String sutieSetupPathName = getPagePathName(wikiPage, TEAR_DOWN_NAME);
			appendMessage(buffer, teardownTag, sutieSetupPathName);

			String suiteTeardownName = SuiteResponder.SUITE_TEARDOWN_NAME;
			includeSuiteSetup(includeSuiteSetup, wikiPage, buffer, teardownTag, suiteTeardownName);
		}

		pageData.setContent(buffer.toString());

		return pageData.getHtml();
	}

	private void includeSuiteSetup(boolean includeSuiteSetup, WikiPage wikiPage, StringBuffer buffer, final String setupTag,
			String suiteSetupName) throws Exception {
		if (includeSuiteSetup) {
			String sutieSetupPathName = getPagePathName(wikiPage, suiteSetupName);
			appendMessage(buffer, setupTag, sutieSetupPathName);
		}
	}

	private String getPagePathName(WikiPage wikiPage, String pageName) throws Exception {
		WikiPage page = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
		if (page == null)
			return null;
		WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(page);
		return PathParser.render(pagePath);
	}

	private void appendMessage(StringBuffer buffer, String tag, String pagePathName) {
		if (pagePathName == null)
			return;
		String logMessage = String.format("!include -%s .", tag);
		buffer.append(logMessage).append(pagePathName).append("\n");
	}

}