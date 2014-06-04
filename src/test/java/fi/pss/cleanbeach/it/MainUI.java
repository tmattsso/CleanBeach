package fi.pss.cleanbeach.it;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainUI extends AbstractPageObject {

    MainUI(WebDriver driver) {
        super(driver);
        // ensure we have a tabbar in our browser
        driver.findElement(By.className("v-touchkit-tabbar"));
    }
}
