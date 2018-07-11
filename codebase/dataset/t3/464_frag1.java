            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

                de.hu_berlin.sam.mmunit.TestModel model = createInitialModel();

                attachModelToResource(model, diagramResource);

                Diagram diagram = ViewService.createDiagram(model, TestModelEditPart.MODEL_ID, MMUnitDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

                if (diagram != null) {

                    diagramResource.getContents().add(diagram);

                    diagram.setName(diagramName);

                    diagram.setElement(model);

                }

                try {

                    diagramResource.save(hub.metrik.lang.eprovide.usertrace.step.diagram.part.MMUnitDiagramEditorUtil.getSaveOptions());

                } catch (IOException e) {

                    MMUnitDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);

                }

                return CommandResult.newOKCommandResult();

            }
