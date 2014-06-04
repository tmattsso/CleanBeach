package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by mattitahvonenitmill on 04/06/14.
 */
public class EventPage extends MainPage {

    public EventPage(WebDriver driver) {
        super(driver);
    }

    public EventPage fillComment(String comment) {
        driver.findElement(By.xpath("//*[text() = 'Comment']")).click();
        driver.findElement(By.tagName("textarea")).sendKeys(comment);
        driver.findElement(By.xpath("//*[@class = 'v-button-caption' and text() = 'Add comment']")).click();
        return this;
    }

    public EventPage verifyCommentPresent(String comment) {
        driver.findElement(By.xpath("//div[text() = '"+comment+"']"));
        return this;
    }
}
