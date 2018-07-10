            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

                ModelRoot model = createInitialModel();

                attachModelToResource(model, modelResource);

                Diagram diagram = ViewService.createDiagram(model, ModelRootEditPart.MODEL_ID, CescsmodelDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

                if (diagram != null) {

                    diagramResource.getContents().add(diagram);

                    diagram.setName(diagramName);

                    diagram.setElement(model);

                }

                try {

                    modelResource.save(cescs.cescsmodel.diagram.part.CescsmodelDiagramEditorUtil.getSaveOptions());

                    diagramResource.save(cescs.cescsmodel.diagram.part.CescsmodelDiagramEditorUtil.getSaveOptions());

                } catch (IOException e) {

                    CescsmodelDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);

                }

                return CommandResult.newOKCommandResult();

            }
