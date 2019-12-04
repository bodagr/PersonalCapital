import Utilities.TestConstans;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.PrintStream;


public class BaseTest {

    BasePage basePage;
    TestConstans data;

    private WebDriver driver;

    public WebDriver getDriver() {
        return this.driver;
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void tearDown() throws FileNotFoundException {
        PrintStream fileOut = new PrintStream(System.getProperty("user.dir")+ "/Reports/" + System.currentTimeMillis() + ".txt");
        System.setOut(fileOut);
        report();
        driver.quit();
    }

    private void report() {
        System.out.println();
        System.out.println("Total links on Website: " + basePage.totalLinksCount);
        System.out.println("Total unique links ==> " + basePage.linkList.size());
        System.out.println("Broken links ==> " + basePage.brokenLinks.size());
        System.out.println();
        System.out.println(">>>>>>>>> List of broken links: <<<<<<<<<");
            for (String link : basePage.brokenLinks)
                System.out.println(link);

    }
}
