import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import org.farng.mp3.MP3File;

import org.w3c.dom.Document;

import org.w3c.dom.NodeList;



public class LyricSOAPClient {



    private static final String alsongUrl = "http://lyrics.alsong.co.kr/alsongwebservice/service1.asmx";



    private HttpURLConnection httpConn;



    private String resultStr = "";
