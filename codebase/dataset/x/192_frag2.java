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
