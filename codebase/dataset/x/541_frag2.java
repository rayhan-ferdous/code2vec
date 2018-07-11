import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.net.URISyntaxException;

import java.net.URL;

import java.util.jar.JarEntry;



/**

 * The class provides a set of methods to pack passed-in entries into the sepcified archive file.

 * the class handle directory output.

 */

public class StaticWeaveDirectoryOutputHandler extends AbstractStaticWeaveOutputHandler {



    private URL source = null;



    private URL target = null;



    /**

     * Construct an instance of StaticWeaveDirectoryOutputHandler.

     * @param source

     * @param target

     */

    public StaticWeaveDirectoryOutputHandler(URL source, URL target) {
