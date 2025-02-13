package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import demo.locators.Locators;

import java.time.Duration;

//Wrapper/helper methods required for automation
@SuppressWarnings({ "unused" })
public class Wrappers {

    static WebDriver driver;
    public static WebDriverWait wait;
    Actions actions;
    final int MAX_WAIT_TIME = 8;
    public final static String HOMEPAGE_URL = "https://www.flipkart.com/";

    // Constructor
    public Wrappers(WebDriver driver) {
        Wrappers.driver = driver;
        this.actions = new Actions(driver);
        Wrappers.wait = new WebDriverWait(driver, Duration.ofSeconds(MAX_WAIT_TIME));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }

    // To print logs of Start and End test case
    public void logStatus(String description, String testCaseID) {
        String timestamp = String.valueOf(java.time.LocalDateTime.now());
        System.out.println(String.format("%s | %s | %s", timestamp, description, testCaseID));

    }

    // Navigate to url
    public void navigateTo(String url) {
        driver.get(url);

    }

    // Search for a product
    public void searchForProduct(String productName) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(Locators.SEARCH_BOX));

            actions.click(element).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE)
                    .perform();

            element.sendKeys(productName, Keys.ENTER);

            // Custom condition to wait until page title contains the product name
            wait.until(driver -> getPageTitle().toLowerCase().contains(productName.toLowerCase()));

        } catch (Exception e) {
            logStatus("Failed to enter text in element: " + e.getMessage(), "" + Locators.SEARCH_BOX);
            e.printStackTrace();
        }

    }

    /*
     * To capture all the search results 
     * There are 2 locators to getSearchResults because the website renders the search results
     *  in two different ways this method will make sure to get the results.
     */
    public List<WebElement> getSearchResults() {
        try {

            List<WebElement> searchResults = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(Locators.SEARCH_RESULTS_ELE));

            return searchResults;

        } catch (TimeoutException e) {
            List<WebElement> searchResults = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(Locators.SEARCH_RESULTS_ELEMENT));
            return searchResults;
        }

    }

    // Sort the search results by a category
    public void sortBy(String categoryToSelect) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(Locators.sortByCategory(categoryToSelect)));

            clickOnElement(Locators.sortByCategory(categoryToSelect));

            // wait for the page to load for the selected category
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(Locators.waitToLoadSelectedCategory(categoryToSelect)));

        } catch (Exception e) {
            logStatus("Failed to sort by " + categoryToSelect, "Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // Selects top 5 product with high customer reviews
    public List<WebElement> topFiveHighReviewProducts() {
        try {
            List<WebElement> searchResults = getSearchResults();
            List<Integer> reviewsInInteger = new ArrayList<>();
            List<WebElement> topFiveProducts = new ArrayList<>();

            for (WebElement searchResult : searchResults) {
                WebElement element = searchResult.findElement(Locators.REVIEW_ELEMENT);
                String reviewText = element.getText();
                Integer reviews = Integer
                        .valueOf(reviewText.substring(1, reviewText.length() - 1).trim().replace(",", ""));

                reviewsInInteger.add(reviews);
            }

            Collections.sort(reviewsInInteger);
            int size = reviewsInInteger.size();

            for (int i = size - 1; i >= size - 5; i--) {
                WebElement element = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(Locators.dyanamicReviewElemenet(reviewsInInteger.get(i))));

                topFiveProducts.add(element);
            }

            return topFiveProducts;

        } catch (Exception e) {
            System.out.println("Failed to get Top five reviewed Products: " + "Error: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    // Select product by ratings
    public List<WebElement> selectByRatings(String ratingsToSelect) {
        List<WebElement> searchResults = getSearchResults();
        List<WebElement> elementsByRating = new ArrayList<WebElement>();
        try {

            for (WebElement element : searchResults) {
                List<WebElement> ratingElement = element.findElements(Locators.getProductEleByRating(ratingsToSelect));

                // If element found with rating add the whole element to the list
                if (!ratingElement.isEmpty()) {
                    elementsByRating.add(element);
                }
            }

            // If no product found for the given rating
            if (elementsByRating.size() == 0) {
                System.out.println("No products found with the given rating: " + ratingsToSelect);
            }

            return elementsByRating;

        } catch (Exception e) {
            logStatus("Failed to get product by rating: "+ratingsToSelect, "Error: " + e.getMessage());
            e.printStackTrace();

            return new ArrayList<WebElement>();
        }

    }

    // Select product by discount %
    public List<WebElement> selectByDiscount(String discountToSelect) {
        try {
            List<WebElement> searchResults = getSearchResults();
            List<WebElement> discountElements = new ArrayList<>();

            for (WebElement element : searchResults) {
                List<WebElement> list = element.findElements(Locators.getProductEleByDiscount(discountToSelect));

                //if list is has elements then add the whole element to list 'discountElements' 
                if (!list.isEmpty()) {
                    discountElements.add(element);
                }
            }

            // If no product found for the given discount
            if (discountElements.size() == 0) {
                System.out.println("No products found with the given discount: " + discountToSelect + "% off");

            }

            return discountElements;

        } catch (Exception e) {
            logStatus("Failed to get search result by discount: "+discountToSelect, "Error: " + e.getMessage());
            e.printStackTrace();

            return new ArrayList<WebElement>();
        }

    }

    //Prints the Title and Discount % of given List<WebElement> 
    public void printTitleAndDiscount(List<WebElement> elements) {
        try {
            for (WebElement ele : elements) {
                logStatus("The Title is: " + getProductTitle(ele), " and its discount is: " + getProductDiscount(ele));
            }

        } catch (Exception e) {
            logStatus("Failed to print Title and Discount", "Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    //Prints the Title and ImageUrl of given List<WebElement> 
    public void printTitleAndImageUrl(List<WebElement> elements) {

        try {
            for (WebElement ele : elements) {
                logStatus("The Title is: " + getCupTitle(ele), " and its Img Url is: " + getImageUrl(ele));

            }

        } catch (Exception e) {
            logStatus("Failed to print Title and ImageUrl ", "Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // Gets the current page title
    public String getPageTitle() {
        return driver.getTitle();

    }

    // Clicks on a WebElement
    public void clickOnElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();

        } catch (Exception e) {
            logStatus("Failed to click on element: ", "Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // Gets the Title of the product - for cup testCase03
    public String getCupTitle(WebElement element) {
        return element.findElement(Locators.TITLE_ELEMENT_CUP).getText();

    }

    // Gets the img url for the element - for cup testCase03
    public String getImageUrl(WebElement element) {
        return getAttributeValue(element.findElement(Locators.IMAGE_ELEMENT_CUP), "src");
    }

    // Gets the Title of the product
    public String getProductTitle(WebElement element) {
        return element.findElement(Locators.TITLE_ELEMENT).getText();

    }

    // Gets the Discount of the product
    public String getProductDiscount(WebElement element) {
        return element.findElement(Locators.DISCOUNT_ELEMENT).getText();

    }

    // Gets the Rating of the product
    public String getProductRating(WebElement element) {
        return element.findElement(Locators.RATING_ELEMENT).getText();

    }

    // Gets the text of WebElement
    public String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText();

        } catch (Exception e) {
            logStatus("Failed to get text from element: "+locator, "Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    // Gets the value of an attribute
    public String getAttributeValue(WebElement element, String attributeName) {
        try {
            return element.getAttribute(attributeName);

        } catch (Exception e) {
            logStatus("Failed to get the value for the attribute " + attributeName, e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    
}
