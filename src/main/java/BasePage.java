import Utilities.TestConstans;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class BasePage {

    @FindBy(tagName = "a")
    private List<WebElement> links;

    private static WebDriver driver;
    public static int totalLinksCount = 0;

    public static SortedSet<String> linkList = new TreeSet<String>();
    public static LinkedList<String> setOfURL = new LinkedList<String>();
    public static Set<String> brokenLinks = new HashSet<String>();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static void openUrl(String url) {
        driver.get(url);
    }

    public void verifyLinks() {
        System.out.println("Total links ==> " + links.size() + " on page: \""  + driver.getCurrentUrl() + "\"");

        for(int i = 0; i < links.size(); i++) {
            WebElement ele = links.get(i);
            String url = ele.getAttribute("href");
            totalLinksCount++;
            if ((url != null) && (url.length() > 0)) {
                int oldSize = linkList.size();

                url = deleteLastChar(url);
                linkList.add(url);

                if (oldSize != linkList.size() && (!url.contains("javascript")) && (!url.contains("mailto")) && (!url.contains("tel:855.855.8005"))) {
                    verifyActiveLink(url);

                    if (url.contains(TestConstans.DOMAIN_RELATIVE_LINKS) && (!url.contains(TestConstans.FACEBOOK_EXCEPTION))
                            && (!url.contains(TestConstans.LINKEDIN_EXCEPTION)) && (!url.contains(TestConstans.SUPPORT_EXCEPTION))
                            && (!url.contains(TestConstans.TEAM_EXCEPTION)) && (!url.contains(TestConstans.TWITTER_EXCEPTION))
                            && (!url.contains(TestConstans.FORBES_EXCEPTION)) && (!url.contains(TestConstans.BLOG_EXCEPTION))
                            && (!url.contains(".pdf"))) {
                                setOfURL.push(url);
                                openUrl(url);
                                verifyLinks();
                    }
                }
            }
        }
        if (setOfURL.size() > 1) {
            setOfURL.pop();
            openUrl(setOfURL.getFirst());
        }

    }

    public String deleteLastChar(String url){
        try {
            if ((url.length() > 1) && (url.charAt(url.length() - 1) == '/')){
                url = url.substring(0, url.length() - 1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Problem with url - "+ url);
        }
        return url;
    }

    public void verifyActiveLink(String url) {
        HttpURLConnection httpURLConnection;
        try {
            URL urlLink = new URL(url);
            httpURLConnection = (HttpURLConnection) urlLink.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                brokenLinks.add(url);
            }
        } catch (IOException e) {

        }
    }
}
