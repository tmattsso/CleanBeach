package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class MainPage {
    
    @FindBy(className = "v-touchkit-tabbar")
    WebElement tabbar;
    
    final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public EventsPage goToEvents() {
        tabButtonWithText("Events").click();
        return new EventsPage(driver);
    }

    private WebElement tabButtonWithText(String caption) {
        return driver.findElement(By.xpath("//div[@class = 'v-touchkit-tabbar-toolbar']//span[text() = '" + caption + "']"));
    }

}
