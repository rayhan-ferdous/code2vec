import java.util.Enumeration;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;



/**

 * Detects if 'User-Agent' header comes from a well-known search engine bot.

 * This is done by matching regular expressions against the header. The

 * regular expressions are provided by a configuration file which is read

 * from the classpath. The list of regular expressions can be extended by

 * just putting additional files under the pre-defined META-INF path.

 * 

 * @author mleidig@schlund.de

 *

 */

public class BotDetector {



    private static final String CONFIG = "META-INF/org/pustefixframework/http/bot-user-agents.txt";



    private static final Pattern pattern = getBotPattern();



    public static boolean isBot(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
