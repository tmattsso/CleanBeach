package fi.pss.cleanbeach.it;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;
import org.openqa.selenium.io.IOUtils;

public class ITSmoke {
    /**
     * Ensures a valid vaadin kickstart page is returned from deployment url.
     * 
     * @throws IOException
     */
    @Test
    public void smokeTest() throws IOException {
        URL url = new URL("http://localhost:8080/cleanbeach/");
        String pageContent = IOUtils.readFully(url.openStream());
        org.junit.Assert.assertTrue("Not init page!", pageContent.contains("Widget"));
    }

}
