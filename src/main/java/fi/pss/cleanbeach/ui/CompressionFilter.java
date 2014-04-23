
package fi.pss.cleanbeach.ui;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import org.mortbay.servlet.GzipFilter;

@WebFilter(urlPatterns = {"/*"})
@WebInitParam(name = "mimeTypes", value = "text/html,text/xml,text/plain,application/javascript,application/json")
public class CompressionFilter extends GzipFilter {

}
