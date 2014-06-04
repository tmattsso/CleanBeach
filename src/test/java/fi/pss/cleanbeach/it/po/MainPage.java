package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class MainPage extends AbstractPageObject {

    public MainPage(WebDriver driver) {
        super(driver);
        // ensure we have a tabbar in our browser
        driver.findElement(By.className("v-touchkit-tabbar"));
    }

    public EventsPage goToEvents() {
        tabButtonWithText("Events").click();
        return new EventsPage(driver);
    }

    private WebElement tabButtonWithText(String caption) {
        return driver.findElement(By.xpath("//div[@class = 'v-touchkit-tabbar-toolbar']//span[text() = '" + caption + "']"));
    }

}
