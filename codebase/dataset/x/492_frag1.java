import java.net.URL;

import java.util.Properties;

import java.util.prefs.Preferences;

import javax.swing.ImageIcon;



/**

/**

 * <h4>ResourceManager</h4>

 * <p>

 * Utility class for managing the resources of the application.

 * </p>

 *

 * @since  Jun 23, 2010

 * @author Christopher K. Allen

 */

public class ResourceManager {



    /** The singleton preferences object */

    private static final Preferences PREFS_CONFIG;



    /** Name of the resources directory relative to the location of <code>MainApplication</code> */

    public static String STR_DIR_RESOURCES = "resources/";



    /**

     * Load the singleton class objects.

     */

    static {

        PREFS_CONFIG = Preferences.userNodeForPackage(ResourceManager.class);
