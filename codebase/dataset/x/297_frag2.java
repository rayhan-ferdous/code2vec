import org.apache.http.params.HttpParams;

import org.apache.http.protocol.HTTP;

import org.apache.http.protocol.HttpContext;



/**

 * TODO Class ShibbolethProxyHandler

 * 

 * @author Lutz Suhrbier (suhrbier@inf.fu-berlin.de)

 * 

 */

public class ShibbolethProxyHandler extends SSLProxyHandler {



    private static final Log logger = LogFactory.getLog(ShibbolethProxyHandler.class);



    private Credentials userCredentials;



    public ShibbolethProxyHandler(HttpHost clientProxy, KeyStore trustStore, KeyStore keyStore, String keyStorePassword, Credentials userCredentials, CookieStore cookieStore) throws SSLKeyException {
