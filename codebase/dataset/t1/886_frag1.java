        IDiagramGraphicalViewer viewer = (IDiagramGraphicalViewer) diagramPart.getViewer();

        final int intialNumOfEditParts = editPartCollector.size();

        if (element instanceof View) {

            EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(element);

            if (editPart != null) {

                editPartCollector.add(editPart);

                return 1;

            }

        }

        String elementID = EMFCoreUtil.getProxyID(element);

        List associatedParts = viewer.findEditPartsForElement(elementID, IGraphicalEditPart.class);

        for (Iterator editPartIt = associatedParts.iterator(); editPartIt.hasNext(); ) {
