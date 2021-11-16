package function;

import fitnesse.wiki.PageData;

public interface Fitness {
    String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception;
}
