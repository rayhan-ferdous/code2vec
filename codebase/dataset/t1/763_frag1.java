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
