    public static void selectElementsInDiagram(IDiagramWorkbenchPart diagramPart, List editParts) {

        diagramPart.getDiagramGraphicalViewer().deselectAll();

        EditPart firstPrimary = null;

        for (Iterator it = editParts.iterator(); it.hasNext(); ) {

            EditPart nextPart = (EditPart) it.next();

            diagramPart.getDiagramGraphicalViewer().appendSelection(nextPart);

            if (firstPrimary == null && nextPart instanceof IPrimaryEditPart) {

                firstPrimary = nextPart;

            }

        }

        if (!editParts.isEmpty()) {

            diagramPart.getDiagramGraphicalViewer().reveal(firstPrimary != null ? firstPrimary : (EditPart) editParts.get(0));

        }

    }



    /**

	 * @generated

	 */

    private static int findElementsInDiagramByID(DiagramEditPart diagramPart, EObject element, List editPartCollector) {

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

            EditPart nextPart = (EditPart) editPartIt.next();

            EditPart parentPart = nextPart.getParent();

            while (parentPart != null && !associatedParts.contains(parentPart)) {

                parentPart = parentPart.getParent();

            }

            if (parentPart == null) {

                editPartCollector.add(nextPart);

            }

        }

        if (intialNumOfEditParts == editPartCollector.size()) {

            if (!associatedParts.isEmpty()) {

                editPartCollector.add(associatedParts.iterator().next());

            } else {

                if (element.eContainer() != null) {

                    return findElementsInDiagramByID(diagramPart, element.eContainer(), editPartCollector);

                }

            }

        }

        return editPartCollector.size() - intialNumOfEditParts;

    }



    /**

	 * @generated

	 */

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



    /**

	 * @generated

	 */

    public static class LazyElement2ViewMap {



        /**

		 * @generated

		 */

        private Map element2ViewMap;



        /**

		 * @generated

		 */

        private View scope;



        /**

		 * @generated

		 */

        private Set elementSet;



        /**

		 * @generated

		 */

        public final List editPartTmpHolder = new ArrayList();



        /**

		 * @generated

		 */

        public LazyElement2ViewMap(View scope, Set elements) {

            this.scope = scope;

            this.elementSet = elements;

        }



        /**

		 * @generated

		 */

        public final Map getElement2ViewMap() {

            if (element2ViewMap == null) {

                element2ViewMap = new HashMap();

                for (Iterator it = elementSet.iterator(); it.hasNext(); ) {

                    EObject element = (EObject) it.next();

                    if (element instanceof View) {

                        View view = (View) element;

                        if (view.getDiagram() == scope.getDiagram()) {

                            element2ViewMap.put(element, element);

                        }

                    }

                }

                buildElement2ViewMap(scope, element2ViewMap, elementSet);

            }

            return element2ViewMap;

        }



        /**

		 * @generated

		 */

        static Map buildElement2ViewMap(View parentView, Map element2ViewMap, Set elements) {

            if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            if (parentView.isSetElement() && !element2ViewMap.containsKey(parentView.getElement()) && elements.contains(parentView.getElement())) {

                element2ViewMap.put(parentView.getElement(), parentView);

                if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            }

            for (Iterator it = parentView.getChildren().iterator(); it.hasNext(); ) {

                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);

                if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            }

            for (Iterator it = parentView.getSourceEdges().iterator(); it.hasNext(); ) {

                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);

                if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            }

            for (Iterator it = parentView.getSourceEdges().iterator(); it.hasNext(); ) {

                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);

                if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            }

            return element2ViewMap;

        }

    }

}
