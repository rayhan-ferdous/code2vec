    public TelcoblocksDiagramEditor() {

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

        new TelcoblocksPaletteFactory().fillPalette(root);

        return root;

    }



    /**

	 * @generated

	 */

    protected PreferencesHint getPreferencesHint() {

        return TelcoblocksServiciosDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

    }



    /**

	 * @generated

	 */

    public String getContributorId() {

        return TelcoblocksServiciosDiagramEditorPlugin.ID;

    }



    /**

	 * @generated

	 */

    public Object getAdapter(Class type) {
