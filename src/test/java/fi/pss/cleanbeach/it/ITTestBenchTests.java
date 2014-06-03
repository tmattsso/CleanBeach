package fi.pss.cleanbeach.it;


import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.commands.TestBenchCommands;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
        driver = TestBench.createDriver(new ChromeDriver());
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
    public void login() {
        MainUI mainUI = new LoginPage(driver).loginWith(TEST_USER,TEST_PW);
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
        // Sleeping for demonstration purposes so that the browser won't close
        // too fast.
        sleep(2000);
        driver.quit();
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
