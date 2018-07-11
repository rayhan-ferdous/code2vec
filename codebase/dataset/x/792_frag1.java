import javax.net.ssl.TrustManagerFactory;

import javax.net.ssl.X509TrustManager;



/**

 * <p>

 * AuthSSLProtocolSocketFactory can be used to validate the identity of the HTTPS 

 * server against a list of trusted certificates and to authenticate to the HTTPS 

 * server using a private key. 

 * </p>

 * 

 * <p>

 * AuthSSLProtocolSocketFactory will enable server authentication when supplied with

 * a {@link KeyStore truststore} file containg one or several trusted certificates. 

 * The client secure socket will reject the connection during the SSL session handshake 

 * if the target HTTPS server attempts to authenticate itself with a non-trusted 

 * certificate.

 * </p>

 * 

 * <p>

 * Use JDK keytool utility to import a trusted certificate and generate a truststore file:    

 *    <pre>

 *     keytool -import -alias "my server cert" -file server.crt -keystore my.truststore

 *    </pre>

 * </p>

 * 

 * <p>

 * AuthSSLProtocolSocketFactory will enable client authentication when supplied with

 * a {@link KeyStore keystore} file containg a private key/public certificate pair. 

 * The client secure socket will use the private key to authenticate itself to the target 

 * HTTPS server during the SSL session handshake if requested to do so by the server. 

 * The target HTTPS server will in its turn verify the certificate presented by the client

 * in order to establish client's authenticity

 * </p>

 * 

 * <p>

 * Use the following sequence of actions to generate a keystore file

 * </p>

 *   <ul>

 *     <li>

 *      <p>

 *      Use JDK keytool utility to generate a new key

 *      <pre>keytool -genkey -v -alias "my client key" -validity 365 -keystore my.keystore</pre>

 *      For simplicity use the same password for the key as that of the keystore

 *      </p>

 *     </li>

 *     <li>

 *      <p>

 *      Issue a certificate signing request (CSR)

 *      <pre>keytool -certreq -alias "my client key" -file mycertreq.csr -keystore my.keystore</pre>

 *     </p>

 *     </li>

 *     <li>

 *      <p>

 *      Send the certificate request to the trusted Certificate Authority for signature. 

 *      One may choose to act as her own CA and sign the certificate request using a PKI 

 *      tool, such as OpenSSL.

 *      </p>

 *     </li>

 *     <li>

 *      <p>

 *       Import the trusted CA root certificate

 *       <pre>keytool -import -alias "my trusted ca" -file caroot.crt -keystore my.keystore</pre> 

 *      </p>

 *     </li>

 *     <li>

 *      <p>

 *       Import the PKCS#7 file containg the complete certificate chain

 *       <pre>keytool -import -alias "my client key" -file mycert.p7 -keystore my.keystore</pre> 

 *      </p>

 *     </li>

 *     <li>

 *      <p>

 *       Verify the content the resultant keystore file

 *       <pre>keytool -list -v -keystore my.keystore</pre> 

 *      </p>

 *     </li>

 *   </ul>

 * <p>

 * Example of using custom protocol socket factory for a specific host:

 *     <pre>

 *     Protocol authhttps = new Protocol("https",  

 *          new AuthSSLProtocolSocketFactory(

 *              new URL("file:my.keystore"), "mypassword",

 *              new URL("file:my.truststore"), "mypassword"), 443); 

 *

 *     HttpClient client = new HttpClient();

 *     client.getHostConfiguration().setHost("localhost", 443, authhttps);

 *     // use relative url only

 *     GetMethod httpget = new GetMethod("/");

 *     client.executeMethod(httpget);

 *     </pre>

 * </p>

 * <p>

 * Example of using custom protocol socket factory per default instead of the standard one:

 *     <pre>

 *     Protocol authhttps = new Protocol("https",  

 *          new AuthSSLProtocolSocketFactory(

 *              new URL("file:my.keystore"), "mypassword",

 *              new URL("file:my.truststore"), "mypassword"), 443); 

 *     Protocol.registerProtocol("https", authhttps);

 *

 *     HttpClient client = new HttpClient();

 *     GetMethod httpget = new GetMethod("https://localhost/");

 *     client.executeMethod(httpget);

 *     </pre>

 * </p>

 * @author <a href="mailto:oleg -at- ural.ru">Oleg Kalnichevski</a>

 * 

 * <p>

 * DISCLAIMER: HttpClient developers DO NOT actively support this component.

 * The component is provided as a reference material, which may be inappropriate

 * for use without additional customization.

 * </p>

 */

public class AuthSSLProtocolSocketFactory implements SecureProtocolSocketFactory {



    /** Log object for this class. */

    private static final Log LOG = LogFactory.getLog(AuthSSLProtocolSocketFactory.class);



    private URL keystoreUrl = null;



    private String keystorePassword = null;



    private URL truststoreUrl = null;
