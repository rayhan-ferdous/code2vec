    public void gotoMarker(IMarker marker) {

        MarkerNavigationService.getInstance().gotoMarker(this, marker);

    }



    /**

	 * @generated

	 */

    public boolean isSaveAsAllowed() {

        return true;

    }



    /**

	 * @generated

	 */

    public void doSaveAs() {

        performSaveAs(new NullProgressMonitor());

    }



    /**

	 * @generated

	 */

    protected void performSaveAs(IProgressMonitor progressMonitor) {

        Shell shell = getSite().getShell();

        IEditorInput input = getEditorInput();

        SaveAsDialog dialog = new SaveAsDialog(shell);

        IFile original = input instanceof IFileEditorInput ? ((IFileEditorInput) input).getFile() : null;

        if (original != null) {

            dialog.setOriginalFile(original);

        }

        dialog.create();

        IDocumentProvider provider = getDocumentProvider();

        if (provider == null) {

            return;

        }

        if (provider.isDeleted(input) && original != null) {

            String message = NLS.bind(Messages.MathDiagramEditor_SavingDeletedFile, original.getName());

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

                MessageDialog.openWarning(shell, Messages.MathDiagramEditor_SaveAsErrorTitle, Messages.MathDiagramEditor_SaveAsErrorMessage);

                return;

            }

        }

        boolean success = false;

        try {

            provider.aboutToChange(newInput);

            getDocumentProvider(newInput).saveDocument(progressMonitor, newInput, getDocumentProvider().getDocument(getEditorInput()), true);

            success = true;

        } catch (CoreException x) {

            IStatus status = x.getStatus();

            if (status == null || status.getSeverity() != IStatus.CANCEL) {

                ErrorDialog.openError(shell, Messages.MathDiagramEditor_SaveErrorTitle, Messages.MathDiagramEditor_SaveErrorMessage, x.getStatus());

            }

        } finally {

            provider.changed(newInput);

            if (success) {

                setInput(newInput);

            }

        }

        if (progressMonitor != null) {

            progressMonitor.setCanceled(!success);

        }

    }



    /**

	 * @generated

	 */

    public ShowInContext getShowInContext() {

        return new ShowInContext(getEditorInput(), getNavigatorSelection());

    }



    /**

	 * @generated

	 */

    private ISelection getNavigatorSelection() {

        IDiagramDocument document = getDiagramDocument();

        if (document == null) {

            return StructuredSelection.EMPTY;

        }

        Diagram diagram = document.getDiagram();

        IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());

        if (file != null) {

            MathNavigatorItem item = new MathNavigatorItem(diagram, file, false);

            return new StructuredSelection(item);

        }

        return StructuredSelection.EMPTY;

    }



    /**

	 * @generated

	 */

    protected void configureGraphicalViewer() {

        super.configureGraphicalViewer();

        DiagramEditorContextMenuProvider provider = new DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());

        getDiagramGraphicalViewer().setContextMenu(provider);

        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
