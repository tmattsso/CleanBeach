package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by mattitahvonenitmill on 03/06/14.
 */
public class LoginPage extends AbstractPageObject {

    private static final String TARGET_URL = "http://localhost:8080/cleanbeach/";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public EventsPage loginWith(String email, String password) {
        driver.get(TARGET_URL);
        WebElement usernameInput = driver.findElement(By.xpath("//input[@type='email']"));
        usernameInput.clear();
        usernameInput.sendKeys(email);

        // Change language to Finnish
        WebElement pwInput = driver.findElement(By.xpath("//input[@type = 'password']"));
        pwInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//*[@class='v-button-caption']"));
        loginButton.click();

        return new EventsPage(driver);
    }

}
