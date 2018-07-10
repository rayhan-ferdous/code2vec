    @Override

    public void doSave(IProgressMonitor monitor) {

        ZoomManager zoomManager = (ZoomManager) this.getActiveEditor().getAdapter(ZoomManager.class);

        double zoom = zoomManager.getZoom();

        this.diagram.setZoom(zoom);

        ERDiagramEditor activeEditor = (ERDiagramEditor) this.getActiveEditor();

        Point location = activeEditor.getLocation();

        this.diagram.setLocation(location.x, location.y);

        Persistent persistent = Persistent.getInstance();

        IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();

        try {

            diagram.getDiagramContents().getSettings().getModelProperties().setUpdatedDate(new Date());

            InputStream source = persistent.createInputStream(this.diagram);

            if (!file.exists()) {

                file.create(source, true, monitor);

            } else {

                file.setContents(source, true, false, monitor);

            }

        } catch (Exception e) {

            Activator.showExceptionDialog(e);

        }

        for (int i = 0; i < this.getPageCount(); i++) {

            IEditorPart editor = this.getEditor(i);

            editor.doSave(monitor);

        }

        validate();

    }
