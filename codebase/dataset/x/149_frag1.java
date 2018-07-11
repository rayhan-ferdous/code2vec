    public Object getAdapter(Class type) {

        if (type == IShowInTargetList.class) {

            return new IShowInTargetList() {



                public String[] getShowInTargetIds() {

                    return new String[] { ProjectExplorer.VIEW_ID };

                }

            };

        }

        return super.getAdapter(type);

    }



    /**

	 * @generated

	 */

    protected IDocumentProvider getDocumentProvider(IEditorInput input) {

        if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {

            return NetworkDiagramEditorPlugin.getInstance().getDocumentProvider();

        }

        return super.getDocumentProvider(input);

    }



    /**

	 * @generated

	 */

    public TransactionalEditingDomain getEditingDomain() {

        IDocument document = getEditorInput() != null ? getDocumentProvider().getDocument(getEditorInput()) : null;

        if (document instanceof IDiagramDocument) {

            return ((IDiagramDocument) document).getEditingDomain();

        }

        return super.getEditingDomain();

    }



    /**

	 * @generated

	 */

    protected void setDocumentProvider(IEditorInput input) {

        if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {

            setDocumentProvider(NetworkDiagramEditorPlugin.getInstance().getDocumentProvider());

        } else {

            super.setDocumentProvider(input);

        }

    }



    /**

	 * @generated

	 */

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

            String message = NLS.bind(Messages.NetworkDiagramEditor_SavingDeletedFile, original.getName());

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

                MessageDialog.openWarning(shell, Messages.NetworkDiagramEditor_SaveAsErrorTitle, Messages.NetworkDiagramEditor_SaveAsErrorMessage);

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

                ErrorDialog.openError(shell, Messages.NetworkDiagramEditor_SaveErrorTitle, Messages.NetworkDiagramEditor_SaveErrorMessage, x.getStatus());

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
