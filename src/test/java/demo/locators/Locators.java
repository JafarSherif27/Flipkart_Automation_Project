package demo.locators;

import org.openqa.selenium.By;

import demo.wrappers.Wrappers;

//Locators required for automation
@SuppressWarnings({ "unused" })
public class Locators {

    // Select the search box
    public final static By SEARCH_BOX = By.xpath("//input[@type='text']");

    // Selects all the search results //washing machine
    public final static By SEARCH_RESULTS_ELE = By.xpath("//div[@class='yKfJKb row']");

    // Selects all the search results //for cups testCase03
    public final static By SEARCH_RESULTS_ELEMENT = By.xpath("//div[@class='slAVV4']");

    // Selects all customer review elements of the product //for cups testCase03
    public final static By REVIEW_ELEMENT = By.xpath(".//div//span[@class='Wphh3N']");

    // Selects the dyanamic review element for the given review in Integer //for cups testCase03
    public static By dyanamicReviewElemenet(Integer intReview) {
        return By.xpath(".//div//span[@class='Wphh3N' and (number(normalize-space(translate(text(),'(,)','')))  = "
                + intReview + ")]");
    }

    // Selects the Product Title -to access this xpath need to use it with a searchResult's element
    public final static By TITLE_ELEMENT = By.xpath(".//div[@class='KzDlHZ']");

    // Selects the Product Title -to access this xpath need to use it with a -cups from top5highestReviewed produt
    public final static By TITLE_ELEMENT_CUP = By.xpath(".//parent::div//preceding-sibling::a[@class='wjcEIp']");

    // Selects Image element -to access this xpath need to use it with a searchResult's element
    public final static By IMAGE_ELEMENT_CUP = By.xpath(".//parent::node()//preceding-sibling::a//img");

    // Selects Discount element -to access this xpath need to use it with a searchResult's element
    public final static By DISCOUNT_ELEMENT = By.xpath(".//div[@class='UkUFwK']//span");

    // Selects Rating element -to access this xpath need to use it with a searchResult's element
    public final static By RATING_ELEMENT = By.xpath(".//div[@class='XQDdHH']");

    // Selects the 4 and above customer rating to sort -cups testCase03
    public final static By SORT_BY_RATING = By.xpath("//div[@class='_6i1qKy' and contains(text(), 4)]");

    // wait until the page loads after sorting by rating
    public final static By WAIT_SORT_BY_RATING = By.className("YcSYyC");

    // Selects product element by ratings -to access this xpath need to use it with a searchResult's element
    public static By getProductEleByRating(String ratingToSelect) {
        return By.xpath(".//div[@class='XQDdHH' and number(normalize-space(text()))" + ratingToSelect + "]");

    }

    // Selects product element by discount% -to access this xpath need to use it with a searchResult's element
    public static By getProductEleByDiscount(String discountToSelect) {
        return By.xpath(".//div[@class='UkUFwK']//span[number(normalize-space(substring-before(text(),'% off') )) "
                + discountToSelect + "]");

    }

    public static By sortByCategory(String categoryToSelect) {
        return By.xpath("//div[contains(@class,'zg-M3Z') and contains(text(), '" + categoryToSelect + "') ]");
    }

    public static By waitToLoadSelectedCategory(String categoryToSelect) {
        return By.xpath("//div[contains(@class,'0H7xSG') and text()='" + categoryToSelect + "']");

    }

    

}
