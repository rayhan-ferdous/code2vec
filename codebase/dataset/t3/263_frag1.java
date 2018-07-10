    protected void render() {

        if (!g3d.checkTranslucent(false)) return;

        imageFontScaling = viewer.getImageFontScaling();

        Measures measures = (Measures) shape;

        doJustify = viewer.getJustifyMeasurements();

        measurementMad = measures.mad;

        font3d = g3d.getFont3DScaled(measures.getFont3D(), imageFontScaling);

        renderPendingMeasurement(measures.measurementPending);

        if (!viewer.getShowMeasurements()) return;

        boolean showMeasurementLabels = viewer.getShowMeasurementLabels();

        boolean dynamicMeasurements = viewer.getDynamicMeasurements();

        measures.setVisibilityInfo();

        for (int i = measures.measurementCount; --i >= 0; ) {

            Measurement m = measures.measurements[i];

            if (dynamicMeasurements || m.isDynamic()) m.refresh();

            if (!m.isVisible()) continue;

            colix = m.getColix();

            if (colix == 0) colix = measures.colix;

            if (colix == 0) colix = viewer.getColixBackgroundContrast();

            g3d.setColix(colix);

            renderMeasurement(m.getCount(), m, showMeasurementLabels);

        }

    }
