import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import org.logicalcobwebs.proxool.ProxoolException;

import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;



public class ProxoolContext implements ProxoolConstants {



    private static Log log = LogFactory.getLog(ProxoolContext.class);



    /**

	 * Registers the proxool by the default config file path.

	 * @throws ProxoolException

	 */

    public static void register() throws ProxoolException {

        register(CONFIG_FILE_PATH);
