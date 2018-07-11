    public static Resource createDiagram(URI diagramURI, URI modelURI, IProgressMonitor progressMonitor) {

        TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();

        progressMonitor.beginTask(Messages.MathDiagramEditorUtil_CreateDiagramProgressTask, 3);

        final Resource diagramResource = editingDomain.getResourceSet().createResource(diagramURI);

        final Resource modelResource = editingDomain.getResourceSet().createResource(modelURI);

        final String diagramName = diagramURI.lastSegment();

        AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain, Messages.MathDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {



            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

                MathDiagram model = createInitialModel();

                attachModelToResource(model, modelResource);

                Diagram diagram = ViewService.createDiagram(model, MathDiagramEditPart.MODEL_ID, MathDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

                if (diagram != null) {

                    diagramResource.getContents().add(diagram);

                    diagram.setName(diagramName);

                    diagram.setElement(model);

                }

                try {

                    modelResource.save(jfb.examples.gmf.math.diagram.part.MathDiagramEditorUtil.getSaveOptions());

                    diagramResource.save(jfb.examples.gmf.math.diagram.part.MathDiagramEditorUtil.getSaveOptions());

                } catch (IOException e) {

                    MathDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);

                }

                return CommandResult.newOKCommandResult();

            }

        };

        try {

            OperationHistoryFactory.getOperationHistory().execute(command, new SubProgressMonitor(progressMonitor, 1), null);

        } catch (ExecutionException e) {

            MathDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);

        }

        setCharset(WorkspaceSynchronizer.getFile(modelResource));

        setCharset(WorkspaceSynchronizer.getFile(diagramResource));

        return diagramResource;

    }
