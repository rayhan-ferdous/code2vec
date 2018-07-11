import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.util.logging.Level;

import java.util.logging.Logger;



/**

 * Used to trace a provided input stream, in which text is still being written,

 * for the presence of a given text pattern.

 */

class OutputTracer extends Thread {



    /** The logger */

    private static Logger logger_ = Logger.getLogger("DTFLogger");



    /** The text to look for in case of a VM crash */

    private static String VM_CRASH_TEXT = "# HotSpot Virtual Machine Error";



    /** The stream. */

    private BufferedReader stream_;



    /** The trace. */

    private PrintWriter trace_;
