            String message = NLS.bind(edu.toronto.cs.openome_model.diagram.part.Messages.Openome_modelDiagramEditor_SavingDeletedFile, original.getName());

            dialog.setErrorMessage(null);

            dialog.setMessage(message, IMessageProvider.WARNING);

        }

        if (dialog.open() == Window.CANCEL) {

            if (progressMonitor != null) {

                progressMonitor.setCanceled(true);

            }

            return;

        }

        IPath filePath = dialog.getResult();

        if (filePath == null) {

            if (progressMonitor != null) {

                progressMonitor.setCanceled(true);

            }

            return;

        }

        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

        IFile file = workspaceRoot.getFile(filePath);

        final IEditorInput newInput = new FileEditorInput(file);

        IEditorMatchingStrategy matchingStrategy = getEditorDescriptor().getEditorMatchingStrategy();

        IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();

        for (int i = 0; i < editorRefs.length; i++) {

            if (matchingStrategy.matches(editorRefs[i], newInput)) {

                MessageDialog.openWarning(shell, edu.toronto.cs.openome_model.diagram.part.Messages.Openome_modelDiagramEditor_SaveAsErrorTitle, edu.toronto.cs.openome_model.diagram.part.Messages.Openome_modelDiagramEditor_SaveAsErrorMessage);
