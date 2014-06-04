package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by mattitahvonenitmill on 04/06/14.
 */
public class EventsPage extends MainPage {
    
    public EventsPage(WebDriver driver) {
        super(driver);
    }

    public void searchFor(String searchTerm) {
        driver.findElement(By.xpath("//*[text() = 'Search']")).click();
        driver.findElement(By.tagName("input")).sendKeys(searchTerm + "\n");
    }

    public EventPage openFirstEvent() {
        driver.findElement(By.xpath("//div[contains(@class, 'v-label-location')]")).click();
        return new EventPage(driver);
    }

}
