package function;
import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTests {
    private String arg;
    private static HtmlTagValidator htmlTagValidator;
    private Boolean expectedValidation;

    public ParameterizedTests(String str, Boolean expectedValidation) {
        this.arg = str;
        this.expectedValidation = expectedValidation;
    }

    @BeforeClass
    public static void initialize() {
        htmlTagValidator = new HtmlTagValidator();
    }

    @Parameters(name = "{index}: {0}, {1}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { "<'br />", false },              // wrong format
                { "img src=\"ar.jpg\">" , false }, // wrong format
                { "<input => />", false },         // wrong format


                { "<br />", true },
                { "<img src=\"a.png\" />", true },
                { "</a>", true } };

        return Arrays.asList(data);
    }

    @Test
    public void test() {
        Boolean res = htmlTagValidator.validate(this.arg);
        String validv = (res) ? "valid" : "invalid";
        System.out.println("HTML tag Format "+arg+ " is " + validv);
        assertEquals("Result", this.expectedValidation, res);

    }
}


class HtmlTagValidator{

    private Pattern pattern;
    private Matcher matcher;

    private static final String HTML_TAG_FORMAT_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

    public HtmlTagValidator(){
        pattern = Pattern.compile(HTML_TAG_FORMAT_PATTERN);
    }


    public boolean validate(final String tag){

        matcher = pattern.matcher(tag);
        return matcher.matches();

    }
}


