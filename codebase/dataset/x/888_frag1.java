        return result;

    }



    private static java.util.Hashtable memory = new java.util.Hashtable();



    /**

   * Saves the state of the model either to a file on the disk or to memory.

   * If the name of the file starts with the prefix "ejs:", then the

   * state of the model will be saved to memory, otherwise it will be

   * dumped to disk.

   * Security considerations apply when running the simulation as

   * an applet.

   * <p>

   * The state of the model is saved by writing to disk all its public

   * fields which implement the java.io.Serializable interface. This

   * includes primitives and arrays.

   * @param _filename the name of a file (either in disk or in memory)

   * @return true if the file was correctly saved

   */

    public boolean saveState(String _filename) {

        if (model == null) {

            return false;

        }

        try {

            java.io.OutputStream out;

            if (_filename.startsWith("ejs:")) {

                out = new java.io.ByteArrayOutputStream();
