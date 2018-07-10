    public static View findView(DiagramEditPart diagramEditPart, EObject targetElement, LazyElement2ViewMap lazyElement2ViewMap) {

        boolean hasStructuralURI = false;

        if (targetElement.eResource() instanceof XMLResource) {

            hasStructuralURI = ((XMLResource) targetElement.eResource()).getID(targetElement) == null;

        }

        View view = null;

        if (hasStructuralURI && !lazyElement2ViewMap.getElement2ViewMap().isEmpty()) {

            view = (View) lazyElement2ViewMap.getElement2ViewMap().get(targetElement);

        } else if (findElementsInDiagramByID(diagramEditPart, targetElement, lazyElement2ViewMap.editPartTmpHolder) > 0) {

            EditPart editPart = (EditPart) lazyElement2ViewMap.editPartTmpHolder.get(0);

            lazyElement2ViewMap.editPartTmpHolder.clear();

            view = editPart.getModel() instanceof View ? (View) editPart.getModel() : null;

        }

        return (view == null) ? diagramEditPart.getDiagramView() : view;

    }
