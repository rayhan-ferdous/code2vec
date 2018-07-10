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
