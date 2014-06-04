package fi.pss.cleanbeach.it.po;

import org.openqa.selenium.WebDriver;

/**
 */
public class AbstractPageObject {

    WebDriver driver;

    AbstractPageObject(WebDriver driver) {
        this.driver = driver;
    }

}
