    public short getStartPageNumber() {

        IViewCursor viewCursor = textDocument.getViewCursorService().getViewCursor();

        IPageCursor pageCursor = viewCursor.getPageCursor();

        if (pageCursor != null) {

            viewCursor.goToRange(getStart(), false);

            return pageCursor.getPage();

        }

        return -1;

    }



    /**

	 * Returns the page number of the text cursor end, returns -1 if page number

	 * could not be determined.

	 * 

	 * @return the page number of the text cursor end, returns -1 if page number

	 *         could not be determined

	 * 

	 * @author Markus Kr�ger

	 */

    public short getEndPageNumber() {
