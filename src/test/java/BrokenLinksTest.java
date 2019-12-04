import Utilities.TestConstans;
import org.testng.annotations.Test;

public class BrokenLinksTest extends BaseTest {

    TestConstans testConst;


    @Test
    public void checkBrokenLinks() {
        BasePage basePage = new BasePage(getDriver());
        basePage.openUrl(testConst.URL);
        basePage.verifyLinks();
    }
}
