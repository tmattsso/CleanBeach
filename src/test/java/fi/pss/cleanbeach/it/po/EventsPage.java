package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 */
public class EventsPage extends MainPage {
    
    @FindBy(xpath = "//*[text() = 'Search']")
    WebElement searchButton;
    
    @FindBy(tagName = "input")
    WebElement searchInput;
    
    @FindBy(xpath = "//div[contains(@class, 'v-label-location')]")
    WebElement firstEventElement;
    
    public EventsPage(WebDriver driver) {
        super(driver);
    }

    public void searchFor(String searchTerm) {
        searchButton.click();
        searchInput.sendKeys(searchTerm + "\n");
        
    }

    public EventPage openFirstEvent() {
        firstEventElement.click();
        return PageFactory.initElements(driver, EventPage.class);
    }

}
