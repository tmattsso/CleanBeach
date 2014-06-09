package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 */
public class LoginPage {

    private static final String TARGET_URL = "http://localhost:8080/cleanbeach/";

    @FindBy(xpath = "//input[@type='email']")
    WebElement usernameInput;
    
    @FindBy(xpath = "//input[@type = 'password']")
    WebElement pwInput;
    
    @FindBy(xpath = "//*[@class='v-button-caption']")
    WebElement loginButton;
    private final WebDriver driver;
    
    public LoginPage(WebDriver driver) {
        driver.get(TARGET_URL);
        this.driver = driver;
    }

    public EventsPage loginWith(String email, String password) {
        usernameInput.clear();
        usernameInput.sendKeys(email);

        // Change language to Finnish
        pwInput.sendKeys(password);
        
        loginButton.click();

        return PageFactory.initElements(driver, EventsPage.class);
    }

}
