    public short getEndPageNumber() {

        IViewCursor viewCursor = textDocument.getViewCursorService().getViewCursor();

        IPageCursor pageCursor = viewCursor.getPageCursor();

        if (pageCursor != null) {

            viewCursor.goToRange(getEnd(), false);

            return pageCursor.getPage();

        }

        return -1;

    }



    /**

	 * Inserts page break at the current cursor position.

	 * 

	 * @throws NOAException

	 *             if the page break can not be set

	 * 

	 * @author Andreas Br�ker

	 * @date 19.09.2006

	 */

    public void insertPageBreak() throws NOAException {
