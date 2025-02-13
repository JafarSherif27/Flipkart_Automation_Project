package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
// import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import demo.wrappers.Wrappers;
import demo.locators.Locators;

@SuppressWarnings({ "unused" })
public class TestCases {
    ChromeDriver driver;
    Wrappers wrappers;
    public static String ratingToSelect;
    public static String discountToSelect;
    WebDriverWait wait; 

    // Test (Implementation of automation)
    /*
     * testCase01: Go to www.flipkart.com. Search "Washing Machine". Sort by popularity and print the count of items with rating less than or equal to 4 stars.
     * 
     * Step 1: Navigate to "www.flipkart.com"
     * Step 2: Search "Washing Machine"
     * Step 3: Sort the results by popularity
     * Step 4: Print the count of items with rating less than or equal to 4
     */
    @Test
    public void testCase01() throws InterruptedException {
        wrappers.logStatus("Start TestCase", "testCase01");

        // Navigate to Filpkart Homepage
        wrappers.navigateTo(Wrappers.HOMEPAGE_URL);

        // Search for "Washing Machine"
        wrappers.searchForProduct("Washing Machine");

        // Sort by Popularity
        wrappers.sortBy("Popularity");

        // Select by rating less than or equal to 4 stars.
        ratingToSelect = "<= 4";
        List<WebElement> productsWithRating = wrappers.selectByRatings(ratingToSelect);

        // Print the number of products found with rating less than or equal to 4 stars.
        if(!productsWithRating.isEmpty()){
            wrappers.logStatus(
                "The number of products with ratings " + ratingToSelect + " : " + productsWithRating.size(),
                "testCase01");
        }


        wrappers.logStatus("End TestCase", "testCase01");

    }


    /*
     * testCase02: Search "iPhone", print the Titles and discount % of items with
     * more than 17% discount
     * 
     * Step 1: Search for "iPhone"
     * Step 2: get products whoose discount is > 17%
     * Step 3: print title and discount % of those products
     */

    @Test
    public void testCase02() {
        wrappers.logStatus("Start TestCase", "testCase02");

        // Search for "iPhone"
        wrappers.searchForProduct("iPhone");

        // Get products whoose discount is >17 % from the search result
        discountToSelect = "> 17";
        List<WebElement> productsWithDiscount = wrappers.selectByDiscount(discountToSelect);


        // Print title and discount % of those products
        if (productsWithDiscount.size() != 0) {
            wrappers.printTitleAndDiscount(productsWithDiscount);
        }


        wrappers.logStatus("End TestCase", "testCase02");

    }


    /*
    testCase03: Search "Coffee Mug", select 4 stars and above, and print the Title and image URL of the 5 items with highest number of reviews
    *
    * Step 1: Search for "Coffee Mug"
    * Step 2: Select 4 stars and above from customer's rating 
    * Step 3: Get the products which has the top 5 highest customer reviews  
    * Step 4: Print the Title and image URL of those 5 items
     */

    @Test
    public void testCase03() {
    wrappers.logStatus("Start TestCase", "testCase03");

    //Search for "Coffee Mug"
    wrappers.searchForProduct("Coffee Mug");

    //Select 4 stars and above and wait for it to load
    wrappers.clickOnElement(Locators.SORT_BY_RATING);
    wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.WAIT_SORT_BY_RATING));

    //Top 5 Products with high review count
    List<WebElement> productsWithHighReview = wrappers.topFiveHighReviewProducts(); 

    // Print title and ImgUrl of those products
    if(!productsWithHighReview.isEmpty()){
        wrappers.printTitleAndImageUrl(productsWithHighReview);
    }

    wrappers.logStatus("End TestCase", "testCase03");

    }



    // Set-Up
    @BeforeTest
    public void startBrowser() {

        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        this.wrappers = new Wrappers(driver);
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    

    // Tear-down
    @AfterTest
    public void endTest() {
    driver.close();
    driver.quit();

    }


}



