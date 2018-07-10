import java.net.URL;

import java.util.logging.Level;

import java.util.logging.Logger;



/**

 * Helper class that eases the task of creating a MEExecutor object to

 * the applications, based on the "blind" parameter that the game receives

 * with the concrete MEExecutor to generate, the way in which the ME is stored

 * and where is this ME stored. Optionally, the MEExecutor can be involved

 * within a decorator.

 * 

 * @author David Llanso

 *

 */

public class MEExecutorFactory {



    /**

	 * This Method creates and load a IMEExecutor from a "blind" string with

	 * the needed parameters.

	 * @param info Needed parameters. format:

	 * 		MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]

	 * 		MEStoredway could be: file|zipfile...

	 * @param idomain Domain of the game to play.

	 * @return The generated MEExecutor with a loaded ME.

	 * @throws ConfigurationException If something went wrong.

	 */

    public static IMEExecutor BuildMEExecutor(String info, gatech.mmpm.IDomain idomain) throws ConfigurationException {

        IMEExecutor meExecutor = null;

        String[] splitInfo = info.split("@@@");

        if ((splitInfo.length < 3) || (splitInfo.length > 4)) throw new ConfigurationException("Unexpected Format. It should be: MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]");
