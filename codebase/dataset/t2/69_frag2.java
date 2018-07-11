    public static boolean openDiagram(Resource diagram) throws PartInitException {

        String path = diagram.getURI().toPlatformString(true);

        IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));

        if (workspaceResource instanceof IFile) {

            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            return null != page.openEditor(new FileEditorInput((IFile) workspaceResource), FilesystemDiagramEditor.ID);

        }

        return false;

    }
