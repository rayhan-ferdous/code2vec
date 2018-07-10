    protected void setDocumentProvider(IEditorInput input) {

        if (input instanceof URIEditorInput) {

            setDocumentProvider(TelcoblocksServiciosDiagramEditorPlugin.getInstance().getDocumentProvider());

        } else {

            super.setDocumentProvider(input);

        }

    }
