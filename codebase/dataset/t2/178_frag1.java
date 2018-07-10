    protected IDocumentProvider getDocumentProvider(IEditorInput input) {

        if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {

            return Fd2DiagramEditorPlugin.getInstance().getDocumentProvider();

        }

        return super.getDocumentProvider(input);

    }
