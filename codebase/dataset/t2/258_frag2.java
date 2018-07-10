    public WebmlDiagramEditor() {

        super(true);

    }



    /**

	 * @generated

	 */

    protected String getContextID() {

        return CONTEXT_ID;

    }



    /**

	 * @generated

	 */

    protected PaletteRoot createPaletteRoot(PaletteRoot existingPaletteRoot) {

        PaletteRoot root = super.createPaletteRoot(existingPaletteRoot);

        new WebmlPaletteFactory().fillPalette(root);

        return root;

    }



    /**

	 * @generated

	 */

    protected PreferencesHint getPreferencesHint() {

        return WebmlDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

    }



    /**

	 * @generated

	 */

    public String getContributorId() {

        return WebmlDiagramEditorPlugin.ID;

    }



    /**

	 * @generated

	 */

    public Object getAdapter(Class type) {
