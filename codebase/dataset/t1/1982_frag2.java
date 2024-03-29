    public static IMarker addMarker(IFile file, String elementId, String location, String message, int statusSeverity) {

        IMarker marker = null;

        try {

            marker = file.createMarker(MARKER_TYPE);

            marker.setAttribute(IMarker.MESSAGE, message);

            marker.setAttribute(IMarker.LOCATION, location);

            marker.setAttribute(org.eclipse.gmf.runtime.common.ui.resources.IMarker.ELEMENT_ID, elementId);

            int markerSeverity = IMarker.SEVERITY_INFO;

            if (statusSeverity == IStatus.WARNING) {

                markerSeverity = IMarker.SEVERITY_WARNING;

            } else if (statusSeverity == IStatus.ERROR || statusSeverity == IStatus.CANCEL) {

                markerSeverity = IMarker.SEVERITY_ERROR;

            }

            marker.setAttribute(IMarker.SEVERITY, markerSeverity);

        } catch (CoreException e) {

            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Failed to create validation marker", e);

        }

        return marker;

    }
