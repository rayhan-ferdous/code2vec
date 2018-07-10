    public static NativeImageFormat createNativeImageFormat(Raster r) {

        NativeImageFormat fmt = new NativeImageFormat();

        SampleModel sm = r.getSampleModel();

        if (sm instanceof ComponentSampleModel) {

            ComponentSampleModel csm = (ComponentSampleModel) sm;

            fmt.cmmFormat = getFormatFromComponentModel(csm, false);

            fmt.scanlineStride = calculateScanlineStrideCSM(csm, r);

        } else if (sm instanceof SinglePixelPackedSampleModel) {

            SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) sm;

            fmt.cmmFormat = getFormatFromSPPSampleModel(sppsm, false);

            fmt.scanlineStride = calculateScanlineStrideSPPSM(sppsm, r);

        }

        if (fmt.cmmFormat == 0) return null;

        fmt.cols = r.getWidth();

        fmt.rows = r.getHeight();

        fmt.dataOffset = r.getDataBuffer().getOffset();

        if (!fmt.setImageData(r.getDataBuffer())) return null;

        return fmt;

    }
