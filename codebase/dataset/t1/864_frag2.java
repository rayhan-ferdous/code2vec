    public IStructuredSelection findSelection(IEditorInput anInput) {

        IDiagramDocument document = UIVocabularyDiagramEditorPlugin.getInstance().getDocumentProvider().getDiagramDocument(anInput);

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            UIVocabularyNavigatorItem item = new UIVocabularyNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;

    }
