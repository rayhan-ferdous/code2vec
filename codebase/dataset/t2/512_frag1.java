        IDiagramDocument document = SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().getDocumentProvider().getDiagramDocument(anInput);

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem item = new SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;
