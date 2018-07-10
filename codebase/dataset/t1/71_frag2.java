    public static final String MARKER_TYPE = se.mdh.mrtc.saveccm.diagram.part.SaveccmDiagramEditorPlugin.ID + ".diagnostic";



    /**

	 * @generated

	 */

    protected void doGotoMarker(IMarker marker) {

        String elementId = marker.getAttribute(org.eclipse.gmf.runtime.common.core.resources.IMarker.ELEMENT_ID, null);

        if (elementId == null || !(getEditor() instanceof DiagramEditor)) {

            return;

        }

        DiagramEditor editor = (DiagramEditor) getEditor();

        Map editPartRegistry = editor.getDiagramGraphicalViewer().getEditPartRegistry();

        EObject targetView = editor.getDiagram().eResource().getEObject(elementId);

        if (targetView == null) {

            return;

        }

        EditPart targetEditPart = (EditPart) editPartRegistry.get(targetView);

        if (targetEditPart != null) {

            se.mdh.mrtc.saveccm.diagram.part.SaveccmDiagramEditorUtil.selectElementsInDiagram(editor, Arrays.asList(new EditPart[] { targetEditPart }));