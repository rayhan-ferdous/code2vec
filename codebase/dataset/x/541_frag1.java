import java.io.OutputStream;

import java.net.URL;

import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import marla.ide.problem.MarlaException;



/**

 * Checks if maRla should be updated

 * @author Ryan Morehart

 */

public class Updater implements Runnable {



    /**

	 * URL to download updater from

	 */

    private final String updateURL;



    /**

	 * Becomes true when maRla exits

	 */

    private static boolean hasExited = false;



    /**

	 * Creates a new updater pointed at the given update server 

	 * @param updateLocation URL which contains the current revision number

	 */

    private Updater(String updateLocation) {

        updateURL = updateLocation;
