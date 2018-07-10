    private static IEditorInput getEditorInput(Diagram diagram) {

        Resource diagramResource = diagram.eResource();

        for (Iterator it = diagramResource.getContents().iterator(); it.hasNext(); ) {

            EObject nextEObject = (EObject) it.next();

            if (nextEObject == diagram) {

                return new FileEditorInput(WorkspaceSynchronizer.getFile(diagramResource));

            }

            if (nextEObject instanceof Diagram) {

                break;

            }

        }

        URI uri = EcoreUtil.getURI(diagram);

        String editorName = uri.lastSegment() + "#" + diagram.eResource().getContents().indexOf(diagram);

        IEditorInput editorInput = new URIEditorInput(uri, editorName);

        return editorInput;

    }



    /**

	 * @generated

	 */

    public IStructuredSelection findSelection(IEditorInput anInput) {

        IDiagramDocument document = PetriDiagramDebuggerPlugin.getInstance().getDocumentProvider().getDiagramDocument(anInput);

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            PetriNavigatorItem item = new PetriNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;

    }



    /**

	 * @generated

	 */

    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
