import xml.completion.CompletionInfo;

import xml.gui.XmlModeToolBar;

import xml.parser.MyEntityResolver.IOExceptionWithLocation;

import errorlist.DefaultErrorSource;

import errorlist.ErrorSource;

import static xml.Debug.*;



/**

 * A SideKick XML parser that uses this under the covers:

 * reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

 *

 * @author kerik-sf

 * @version $Id: XercesParserImpl.java 21148 2012-02-18 15:09:52Z kerik-sf $

 */

public class XercesParserImpl extends XmlParser {



    public static String COMPLETION_INFO_CACHE_ENTRY = "CompletionInfo";



    private View view = null;



    private Map<View, JPanel> panels = new HashMap<View, JPanel>();



    public XercesParserImpl() {

        super("xml");

    }



    @Override

    public void activate(View view) {

        this.view = view;

    }



    /**

	 * a buffer read lock is hold arround parse()

	 */

    public SideKickParsedData parse(Buffer buffer, DefaultErrorSource errorSource) {

        long start = System.currentTimeMillis();

        Log.log(Log.NOTICE, XercesParserImpl.class, "parsing started @" + start);
