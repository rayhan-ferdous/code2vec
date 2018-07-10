            vertexPosition = this.wwd.getModel().getGlobe().computePositionFromPoint(vert);

            controlPoint = new Ellipsoid(vertexPosition, radius, radius, radius);

            controlPoint.setAttributes(this.scaleControlAttributes);

            controlPoint.setAltitudeMode(this.getAltitudeMode());

            controlPoint.setValue(AVKey.ACTION, SCALE_DOWN_ACTION);
