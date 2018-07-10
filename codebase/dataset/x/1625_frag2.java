        String unescaped = replace(input, "&", "&amp;");

        unescaped = replace(unescaped, "<", "&lt;");

        unescaped = replace(unescaped, ">", "&gt;");

        return unescaped;

    }



    /**

     * Return an escaped string where &lt;meta, &lt;link tags are escaped

     *

     * @param input Unescaped string

     * @return Escaped string where &lt;meta, &lt;link tags are escaped

     */

    public static String escapeMetaAndLink(String input) {

        if (input == null) {

            return null;

        }

        String cleanedInput = input.replaceAll("<[mM][eE][tT][aA]", "&lt;meta");
