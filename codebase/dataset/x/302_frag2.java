import org.jdom.JDOMException;



/**

 * Sends workitem related messages to a

 *

 * 

 * @author Lachlan Aldred

 * Date: 16/09/2005

 * Time: 15:34:59

 */

public class InterfaceD_Client extends Interface_Client {



    private String _interfaceDServerURI;



    public InterfaceD_Client(String interfaceDServerURI) {

        _interfaceDServerURI = interfaceDServerURI;

    }



    /**

     * Permits the sending of WorkItemRecords.

     * @param urlStr

     * @param paramsMap

     * @param attribute

     * @return A success or failure message

     * @throws IOException

     */

    public static String executePost(String urlStr, Map paramsMap, WorkItemRecord attribute) throws IOException {

        StringBuffer result = new StringBuffer();

        HttpURLConnection connection = null;

        URL url = new URL(urlStr);

        connection = (HttpURLConnection) url.openConnection();
