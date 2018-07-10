        return new DrawingView[] { view() };

    }



    /**

	 * Sets the default tool of the editor.

	 * @see DrawingEditor

	 */

    public void toolDone() {

        setTool(fDefaultToolButton.tool(), fDefaultToolButton.name());

        setSelected(fDefaultToolButton);

    }



    /**

	 * Handles a change of the current selection. Updates all

	 * menu items that are selection sensitive.

	 * @see DrawingEditor

	 */

    public void figureSelectionChanged(DrawingView view) {

        setupAttributes();

    }



    public void viewSelectionChanged(DrawingView oldView, DrawingView newView) {
