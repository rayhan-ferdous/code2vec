    protected void render() {

        if (!g3d.checkTranslucent(false)) return;

        Measures measures = (Measures) shape;

        doJustify = viewer.getJustifyMeasurements();

        mad = measures.mad;

        imageFontScaling = viewer.getImageFontScaling();

        font3d = g3d.getFont3DScaled(measures.font3d, imageFontScaling);

        renderPendingMeasurement(measures.measurementPending);

        if (!viewer.getShowMeasurements()) return;

        boolean showMeasurementLabels = viewer.getShowMeasurementLabels();

        boolean dynamicMeasurements = viewer.getDynamicMeasurements();

        measures.setVisibilityInfo();

        for (int i = measures.measurementCount; --i >= 0; ) {

            Measurement m = measures.measurements.get(i);

            if (dynamicMeasurements || m.isDynamic()) m.refresh();

            if (!m.isVisible()) continue;

            colix = m.getColix();

            if (colix == 0) colix = measures.colix;

            if (colix == 0) colix = viewer.getColixBackgroundContrast();

            g3d.setColix(colix);

            renderMeasurement(m.getCount(), m, showMeasurementLabels);

        }

    }
