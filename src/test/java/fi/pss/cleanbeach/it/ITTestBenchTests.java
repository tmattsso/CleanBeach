package fi.pss.cleanbeach.it;


import fi.pss.cleanbeach.it.po.LoginPage;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.commands.TestBenchCommands;
import fi.pss.cleanbeach.it.po.EventPage;
import fi.pss.cleanbeach.it.po.EventsPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.PageFactory;

/**
 * This is an example if an automated integration/acceptance test for a TouchKit
 * application using Vaadin TestBench.
 */
public class ITTestBenchTests {

    private static final String TEST_USER = "demo@demo.com";
    private static final String TEST_PW = "vaadin";

    static WebDriver driver;
    
    @BeforeClass
    public static void setup() {
        ChromeDriver d = new ChromeDriver();
        d.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        driver = TestBench.createDriver(d);
        driver.manage().window().setSize(new Dimension(320, 640));
    }

    /**
     * Runs the test with Google Chrome. Chrome has an excellent WebDriver
     * support, there are versions for varios platforms and it uses the same
     * rendering engine as most mobile platforms. That is why it is an excellent
     * help for developers working with mobile application testing.
     * 
     * Also during actual automated testing phase it can be used as a low cost
     * fallback for real mobile devices if one cannot afford to make tests run
     * on real devices or on emulators/simulators.
     */
    @Test
    public void commentOnVaadinEvent() {
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        EventsPage eventsPage = loginPage.loginWith(TEST_USER, TEST_PW);
        eventsPage.searchFor("vaadin");
        EventPage firstEvent = eventsPage.openFirstEvent();
        final String comment = "Is there some litter at Vaadin HQ?";
        firstEvent.fillComment(comment);
        firstEvent.verifyCommentPresent(comment);
        reportExecutionTime();
    }

	private void reportExecutionTime() {
		TestBenchCommands tbd = (TestBenchCommands) TestBench.createDriver(driver);
        long totalTimeSpentRendering = tbd.totalTimeSpentRendering();
        long totalTimeSpentServicingRequests = tbd
                .totalTimeSpentServicingRequests();
        System.out.println(String.format(
                "Processing times:\n*******************\n\t Client: %d \n\t Server: %d ",
                totalTimeSpentRendering, totalTimeSpentServicingRequests));
	}

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
