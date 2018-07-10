        IDiagramDocument document = dataflowScheme.diagram.part.ModelDiagramEditorPlugin.getInstance().getDocumentProvider().getDiagramDocument(anInput);

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            dataflowScheme.diagram.navigator.ModelNavigatorItem item = new dataflowScheme.diagram.navigator.ModelNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;
