package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 */
public class EventPage extends MainPage {

    @FindBy(xpath = "//*[text() = 'Comment']")
    WebElement commentActionButton;

    @FindBy(tagName = "textarea")
    WebElement commentTextArea;

    @FindBy(xpath = "//*[@class = 'v-button-caption' and text() = 'Add comment']")
    WebElement addCommentButton;
    
    @FindBy(xpath = "//*[text() = 'Join event']")
    WebElement joinEventButton;

    public EventPage(WebDriver driver) {
        super(driver);
    }

    public EventPage fillComment(String comment) {
        try {
            joinEvent();
        } catch (Exception e) {
            // Already joined
        }
        commentActionButton.click();
        commentTextArea.sendKeys(comment);
        addCommentButton.click();
        return this;
    }

    private void joinEvent() {
        joinEventButton.click();
    }

    public EventPage verifyCommentPresent(String comment) {
        driver.findElement(By.xpath("//div[text() = '" + comment + "']"));
        return this;
    }
}
