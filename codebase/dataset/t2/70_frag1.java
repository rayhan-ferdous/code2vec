public class TestNavigatorLinkHelper implements ILinkHelper {



    /**

	 * @generated

	 */

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

        IDiagramDocument document = TestDiagramEditorPlugin.getInstance().getDocumentProvider().getDiagramDocument(anInput);

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            TestNavigatorItem item = new TestNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;

    }



    /**

	 * @generated

	 */

    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {

        if (aSelection == null || aSelection.isEmpty()) {

            return;

        }

        if (false == aSelection.getFirstElement() instanceof TestAbstractNavigatorItem) {

            return;

        }

        TestAbstractNavigatorItem abstractNavigatorItem = (TestAbstractNavigatorItem) aSelection.getFirstElement();

        View navigatorView = null;

        if (abstractNavigatorItem instanceof TestNavigatorItem) {

            navigatorView = ((TestNavigatorItem) abstractNavigatorItem).getView();

        } else if (abstractNavigatorItem instanceof TestNavigatorGroup) {

            TestNavigatorGroup navigatorGroup = (TestNavigatorGroup) abstractNavigatorItem;

            if (navigatorGroup.getParent() instanceof TestNavigatorItem) {

                navigatorView = ((TestNavigatorItem) navigatorGroup.getParent()).getView();

            }

        }

        if (navigatorView == null) {

            return;

        }

        IEditorInput editorInput = getEditorInput(navigatorView.getDiagram());

        IEditorPart editor = aPage.findEditor(editorInput);

        if (editor == null) {

            return;

        }

        aPage.bringToTop(editor);

        if (editor instanceof DiagramEditor) {

            DiagramEditor diagramEditor = (DiagramEditor) editor;

            ResourceSet diagramEditorResourceSet = diagramEditor.getEditingDomain().getResourceSet();

            EObject selectedView = diagramEditorResourceSet.getEObject(EcoreUtil.getURI(navigatorView), true);

            if (selectedView == null) {

                return;

            }

            GraphicalViewer graphicalViewer = (GraphicalViewer) diagramEditor.getAdapter(GraphicalViewer.class);

            EditPart selectedEditPart = (EditPart) graphicalViewer.getEditPartRegistry().get(selectedView);

            if (selectedEditPart != null) {

                graphicalViewer.select(selectedEditPart);

            }

        }

    }

}
