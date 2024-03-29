package org.apache.http.protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.mockup.TestHttpClient;
import org.apache.http.mockup.TestHttpServer;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import junit.framework.*;
import org.apache.http.HttpConnectionMetrics;

public class TestHttpServiceAndExecutor extends TestCase {

    public TestHttpServiceAndExecutor(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestHttpServiceAndExecutor.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(TestHttpServiceAndExecutor.class);
    }

    private TestHttpServer server;

    private TestHttpClient client;

    protected void setUp() throws Exception {
        this.server = new TestHttpServer();
        this.client = new TestHttpClient();
    }

    protected void tearDown() throws Exception {
        this.server.shutdown();
    }

    /**
     * This test case executes a series of simple GET requests 
     */
    public void testSimpleBasicHttpRequests() throws Exception {
        int reqNo = 20;
        Random rnd = new Random();
        final List testData = new ArrayList(reqNo);
        for (int i = 0; i < reqNo; i++) {
            int size = rnd.nextInt(5000);
            byte[] data = new byte[size];
            rnd.nextBytes(data);
            testData.add(data);
        }
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                String s = request.getRequestLine().getUri();
                if (s.startsWith("/?")) {
                    s = s.substring(2);
                }
                int index = Integer.parseInt(s);
                byte[] data = (byte[]) testData.get(index);
                ByteArrayEntity entity = new ByteArrayEntity(data);
                response.setEntity(entity);
            }
        });
        this.server.start();
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpRequest get = new BasicHttpRequest("GET", "/?" + r);
                HttpResponse response = this.client.execute(get, host, conn);
                byte[] received = EntityUtils.toByteArray(response.getEntity());
                byte[] expected = (byte[]) testData.get(r);
                assertEquals(expected.length, received.length);
                for (int i = 0; i < expected.length; i++) {
                    assertEquals(expected[i], received[i]);
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }

    /**
     * This test case executes a series of simple POST requests with content length 
     * delimited content. 
     */
    public void testSimpleHttpPostsWithContentLength() throws Exception {
        int reqNo = 20;
        Random rnd = new Random();
        List testData = new ArrayList(reqNo);
        for (int i = 0; i < reqNo; i++) {
            int size = rnd.nextInt(5000);
            byte[] data = new byte[size];
            rnd.nextBytes(data);
            testData.add(data);
        }
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity incoming = ((HttpEntityEnclosingRequest) request).getEntity();
                    byte[] data = EntityUtils.toByteArray(incoming);
                    ByteArrayEntity outgoing = new ByteArrayEntity(data);
                    outgoing.setChunked(false);
                    response.setEntity(outgoing);
                } else {
                    StringEntity outgoing = new StringEntity("No content");
                    response.setEntity(outgoing);
                }
            }
        });
        this.server.start();
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpEntityEnclosingRequest post = new BasicHttpEntityEnclosingRequest("POST", "/");
                byte[] data = (byte[]) testData.get(r);
                ByteArrayEntity outgoing = new ByteArrayEntity(data);
                post.setEntity(outgoing);
                HttpResponse response = this.client.execute(post, host, conn);
                byte[] received = EntityUtils.toByteArray(response.getEntity());
                byte[] expected = (byte[]) testData.get(r);
                assertEquals(expected.length, received.length);
                for (int i = 0; i < expected.length; i++) {
                    assertEquals(expected[i], received[i]);
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }

    /**
     * This test case executes a series of simple POST requests with chunk 
     * coded content content. 
     */
    public void testSimpleHttpPostsChunked() throws Exception {
        int reqNo = 20;
        Random rnd = new Random();
        List testData = new ArrayList(reqNo);
        for (int i = 0; i < reqNo; i++) {
            int size = rnd.nextInt(20000);
            byte[] data = new byte[size];
            rnd.nextBytes(data);
            testData.add(data);
        }
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity incoming = ((HttpEntityEnclosingRequest) request).getEntity();
                    byte[] data = EntityUtils.toByteArray(incoming);
                    ByteArrayEntity outgoing = new ByteArrayEntity(data);
                    outgoing.setChunked(true);
                    response.setEntity(outgoing);
                } else {
                    StringEntity outgoing = new StringEntity("No content");
                    response.setEntity(outgoing);
                }
            }
        });
        this.server.start();
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpEntityEnclosingRequest post = new BasicHttpEntityEnclosingRequest("POST", "/");
                byte[] data = (byte[]) testData.get(r);
                ByteArrayEntity outgoing = new ByteArrayEntity(data);
                outgoing.setChunked(true);
                post.setEntity(outgoing);
                HttpResponse response = this.client.execute(post, host, conn);
                byte[] received = EntityUtils.toByteArray(response.getEntity());
                byte[] expected = (byte[]) testData.get(r);
                assertEquals(expected.length, received.length);
                for (int i = 0; i < expected.length; i++) {
                    assertEquals(expected[i], received[i]);
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }

    /**
     * This test case executes a series of simple HTTP/1.0 POST requests. 
     */
    public void testSimpleHttpPostsHTTP10() throws Exception {
        int reqNo = 20;
        Random rnd = new Random();
        List testData = new ArrayList(reqNo);
        for (int i = 0; i < reqNo; i++) {
            int size = rnd.nextInt(5000);
            byte[] data = new byte[size];
            rnd.nextBytes(data);
            testData.add(data);
        }
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity incoming = ((HttpEntityEnclosingRequest) request).getEntity();
                    byte[] data = EntityUtils.toByteArray(incoming);
                    ByteArrayEntity outgoing = new ByteArrayEntity(data);
                    outgoing.setChunked(false);
                    response.setEntity(outgoing);
                } else {
                    StringEntity outgoing = new StringEntity("No content");
                    response.setEntity(outgoing);
                }
            }
        });
        this.server.start();
        this.client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpEntityEnclosingRequest post = new BasicHttpEntityEnclosingRequest("POST", "/");
                byte[] data = (byte[]) testData.get(r);
                ByteArrayEntity outgoing = new ByteArrayEntity(data);
                post.setEntity(outgoing);
                HttpResponse response = this.client.execute(post, host, conn);
                assertEquals(HttpVersion.HTTP_1_0, response.getStatusLine().getProtocolVersion());
                byte[] received = EntityUtils.toByteArray(response.getEntity());
                byte[] expected = (byte[]) testData.get(r);
                assertEquals(expected.length, received.length);
                for (int i = 0; i < expected.length; i++) {
                    assertEquals(expected[i], received[i]);
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }

    /**
     * This test case executes a series of simple POST requests using 
     * the 'expect: continue' handshake. 
     */
    public void testHttpPostsWithExpectContinue() throws Exception {
        int reqNo = 20;
        Random rnd = new Random();
        List testData = new ArrayList(reqNo);
        for (int i = 0; i < reqNo; i++) {
            int size = rnd.nextInt(5000);
            byte[] data = new byte[size];
            rnd.nextBytes(data);
            testData.add(data);
        }
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity incoming = ((HttpEntityEnclosingRequest) request).getEntity();
                    byte[] data = EntityUtils.toByteArray(incoming);
                    ByteArrayEntity outgoing = new ByteArrayEntity(data);
                    outgoing.setChunked(true);
                    response.setEntity(outgoing);
                } else {
                    StringEntity outgoing = new StringEntity("No content");
                    response.setEntity(outgoing);
                }
            }
        });
        this.server.start();
        this.client.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpEntityEnclosingRequest post = new BasicHttpEntityEnclosingRequest("POST", "/");
                byte[] data = (byte[]) testData.get(r);
                ByteArrayEntity outgoing = new ByteArrayEntity(data);
                outgoing.setChunked(true);
                post.setEntity(outgoing);
                HttpResponse response = this.client.execute(post, host, conn);
                byte[] received = EntityUtils.toByteArray(response.getEntity());
                byte[] expected = (byte[]) testData.get(r);
                assertEquals(expected.length, received.length);
                for (int i = 0; i < expected.length; i++) {
                    assertEquals(expected[i], received[i]);
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }

    /**
     * This test case executes a series of simple POST requests that do not 
     * meet the target server expectations. 
     */
    public void testHttpPostsWithExpectationVerification() throws Exception {
        int reqNo = 3;
        this.server.registerHandler("*", new HttpRequestHandler() {

            public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                StringEntity outgoing = new StringEntity("No content");
                response.setEntity(outgoing);
            }
        });
        this.server.setExpectationVerifier(new HttpExpectationVerifier() {

            public void verify(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException {
                Header someheader = request.getFirstHeader("Secret");
                if (someheader != null) {
                    int secretNumber;
                    try {
                        secretNumber = Integer.parseInt(someheader.getValue());
                    } catch (NumberFormatException ex) {
                        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        return;
                    }
                    if (secretNumber < 2) {
                        response.setStatusCode(HttpStatus.SC_EXPECTATION_FAILED);
                        ByteArrayEntity outgoing = new ByteArrayEntity(EncodingUtils.getAsciiBytes("Wrong secret number"));
                        response.setEntity(outgoing);
                    }
                }
            }
        });
        this.server.start();
        this.client.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        HttpHost host = new HttpHost("localhost", this.server.getPort());
        try {
            for (int r = 0; r < reqNo; r++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, this.client.getParams());
                }
                BasicHttpEntityEnclosingRequest post = new BasicHttpEntityEnclosingRequest("POST", "/");
                post.addHeader("Secret", Integer.toString(r));
                ByteArrayEntity outgoing = new ByteArrayEntity(EncodingUtils.getAsciiBytes("No content"));
                post.setEntity(outgoing);
                HttpResponse response = this.client.execute(post, host, conn);
                HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                entity.consumeContent();
                if (r < 2) {
                    assertEquals(HttpStatus.SC_EXPECTATION_FAILED, response.getStatusLine().getStatusCode());
                } else {
                    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                }
                if (!this.client.keepAlive(response)) {
                    conn.close();
                }
            }
            HttpConnectionMetrics cm = conn.getMetrics();
            assertEquals(reqNo, cm.getRequestCount());
            assertEquals(reqNo, cm.getResponseCount());
        } finally {
            conn.close();
            this.server.shutdown();
        }
    }
}
