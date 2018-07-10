    public String _right(String rText, int rColWidth) {

        return _align(rText, " ", rColWidth, 'r');

    }



    /** Center align String with spaces. */

    public String _center(String rText, int rColWidth) {

        return _align(rText, " ", rColWidth, 'c');

    }



    /** Align text within background text to specified column width.

  *  Alignment can be 'l': left, 'c': center, 'r': right

  */

    public String _align(String rText, String rBackText, int rColWidth, char rAlignment) {
