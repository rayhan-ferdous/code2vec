        if (document instanceof IDiagramDocument) {

            return ((IDiagramDocument) document).getEditingDomain();

        }

        return super.getEditingDomain();

    }



    /**

	 * @generated

	 */

    protected void setDocumentProvider(IEditorInput input) {

        if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {

            setDocumentProvider(FilesystemDiagramEditorPlugin.getInstance().getDocumentProvider());

        } else {

            super.setDocumentProvider(input);

        }

    }



    /**

	 * @generated

	 */

    public void gotoMarker(IMarker marker) {

        MarkerNavigationService.getInstance().gotoMarker(this, marker);

    }



    /**

	 * @generated

	 */

    public boolean isSaveAsAllowed() {
