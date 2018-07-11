    public static boolean openDiagram(Resource diagram) throws PartInitException {

        String path = diagram.getURI().toPlatformString(true);

        IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));

        if (workspaceResource instanceof IFile) {

            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            return null != page.openEditor(new FileEditorInput((IFile) workspaceResource), PlwebDiagramEditor.ID);

        }

        return false;

    }



    /**

	 * @generated

	 */

    public static void setCharset(IFile file) {

        if (file == null) {

            return;

        }

        try {

            file.setCharset("UTF-8", new NullProgressMonitor());

        } catch (CoreException e) {

            PlwebDiagramEditorPlugin.getInstance().logError("Unable to set charset for file " + file.getFullPath(), e);

        }

    }



    /**

	 * @generated

	 */

    public static String getUniqueFileName(IPath containerFullPath, String fileName, String extension) {

        if (containerFullPath == null) {

            containerFullPath = new Path("");

        }

        if (fileName == null || fileName.trim().length() == 0) {

            fileName = "default";

        }

        IPath filePath = containerFullPath.append(fileName);

        if (extension != null && !extension.equals(filePath.getFileExtension())) {

            filePath = filePath.addFileExtension(extension);

        }

        extension = filePath.getFileExtension();

        fileName = filePath.removeFileExtension().lastSegment();

        int i = 1;

        while (ResourcesPlugin.getWorkspace().getRoot().exists(filePath)) {

            i++;

            filePath = containerFullPath.append(fileName + i);

            if (extension != null) {

                filePath = filePath.addFileExtension(extension);

            }

        }

        return filePath.lastSegment();

    }



    /**

	 * Runs the wizard in a dialog.

	 * 

	 * @generated

	 */

    public static void runWizard(Shell shell, Wizard wizard, String settingsKey) {

        IDialogSettings pluginDialogSettings = PlwebDiagramEditorPlugin.getInstance().getDialogSettings();

        IDialogSettings wizardDialogSettings = pluginDialogSettings.getSection(settingsKey);

        if (wizardDialogSettings == null) {

            wizardDialogSettings = pluginDialogSettings.addNewSection(settingsKey);

        }

        wizard.setDialogSettings(wizardDialogSettings);

        WizardDialog dialog = new WizardDialog(shell, wizard);

        dialog.create();

        dialog.getShell().setSize(Math.max(500, dialog.getShell().getSize().x), 500);

        dialog.open();

    }



    /**

	 * This method should be called within a workspace modify operation since it

	 * creates resources.

	 * 

	 * @generated

	 */

    public static Resource createDiagram(URI diagramURI, URI modelURI, IProgressMonitor progressMonitor) {

        TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();

        progressMonitor.beginTask(Messages.PlwebDiagramEditorUtil_CreateDiagramProgressTask, 3);

        final Resource diagramResource = editingDomain.getResourceSet().createResource(diagramURI);

        final Resource modelResource = editingDomain.getResourceSet().createResource(modelURI);

        final String diagramName = diagramURI.lastSegment();

        AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain, Messages.PlwebDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {



            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

                DiagramRoot model = createInitialModel();

                attachModelToResource(model, modelResource);

                Diagram diagram = ViewService.createDiagram(model, DiagramRootEditPart.MODEL_ID, PlwebDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

                if (diagram != null) {

                    diagramResource.getContents().add(diagram);

                    diagram.setName(diagramName);

                    diagram.setElement(model);

                }

                try {

                    modelResource.save(org.spbu.plweb.diagram.part.PlwebDiagramEditorUtil.getSaveOptions());

                    diagramResource.save(org.spbu.plweb.diagram.part.PlwebDiagramEditorUtil.getSaveOptions());

                } catch (IOException e) {

                    PlwebDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);

                }

                return CommandResult.newOKCommandResult();

            }

        };

        try {

            OperationHistoryFactory.getOperationHistory().execute(command, new SubProgressMonitor(progressMonitor, 1), null);

        } catch (ExecutionException e) {

            PlwebDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);

        }

        setCharset(WorkspaceSynchronizer.getFile(modelResource));

        setCharset(WorkspaceSynchronizer.getFile(diagramResource));

        return diagramResource;

    }



    /**

	 * Create a new instance of domain element associated with canvas. <!--

	 * begin-user-doc --> <!-- end-user-doc -->

	 * 

	 * @generated

	 */

    private static DiagramRoot createInitialModel() {

        return PlwebFactory.eINSTANCE.createDiagramRoot();

    }



    /**

	 * Store model element in the resource.

	 * <!-- begin-user-doc --> <!--

	 * end-user-doc -->

	 * @generated

	 */

    private static void attachModelToResource(DiagramRoot model, Resource resource) {
