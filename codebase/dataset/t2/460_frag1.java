    protected void scaleShapeEastWest(Point previousMousePoint, Point mousePoint, String scaleDirection) {

        Position referencePos = this.shape.getReferencePosition();

        if (referencePos == null) return;

        Vec4 referencePoint = this.wwd.getModel().getGlobe().computePointFromPosition(referencePos);

        Line screenRay = this.wwd.getView().computeRayFromScreenPoint(mousePoint.getX(), mousePoint.getY());

        Line previousScreenRay = this.wwd.getView().computeRayFromScreenPoint(previousMousePoint.getX(), previousMousePoint.getY());

        Vec4 nearestPointOnLine = screenRay.nearestPointTo(referencePoint);

        Vec4 previousNearestPointOnLine = previousScreenRay.nearestPointTo(referencePoint);

        Position controlPosition = this.controlPoints.get(0).getCenterPosition();

        Vec4 controlPoint = this.wwd.getModel().getGlobe().computePointFromPosition(controlPosition);
