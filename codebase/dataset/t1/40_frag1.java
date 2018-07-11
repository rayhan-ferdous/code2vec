        DirWriter writer = fact.newDirWriter(dirFile, uid, id, readMeFile, readMeCharset, encodeParam());

        try {

            build(writer, args, off);

        } finally {

            writer.close();

        }

    }



    /**

     *  Description of the Method

     *

     * @param  args             Description of the Parameter

     * @param  off              Description of the Parameter

     * @exception  IOException  Description of the Exception

     */

    public void append(String[] args, int off) throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        try {

            build(writer, args, off);

        } finally {

            writer.close();

        }

    }



    private void addDirBuilderPrefElem(HashMap map, String key) {
