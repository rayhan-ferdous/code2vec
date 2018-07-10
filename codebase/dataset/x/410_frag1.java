import sfljtse.tsf.coreLayer.quotes.timeframes.ATimeFrame;

import sfljtse.tsf.coreLayer.utils.FileUtils;

import sfljtse.tsf.coreLayer.settings.Settings;

import sun.management.FileSystem;



/**

 * @title		: YahooFetcher       

 * @description	:  

 * @date		: 27-apr-2006   

 * @author		: Alberto Sfolcini  <a.sfolcini@gmail.com>

 */

public class YahooFetcher extends AQuoteFetcher {



    private boolean debug = true;



    /**

     * The URL string has been built in according with the following page:

     * http://www.gummy-stuff.org/Yahoo-data.htm

     * 

     */

    private String provider = "http://quote.yahoo.com/d/quotes.csv?s=";



    private String tags = "&f=sl1d1t1ohgv&e=.csv";
