    public void drawCurves() {

        float[] from = new float[1];

        float[] to = new float[1];

        Flist bpts = new Flist();

        qlist.getRange(from, to, bpts);

        renderhints.init();

        backend.bgncurv();

        for (int i = bpts.start; i < bpts.end - 1; i++) {

            float[] pta = new float[1];

            float[] ptb = new float[1];

            pta[0] = bpts.pts[i];

            ptb[0] = bpts.pts[i + 1];

            qlist.downloadAll(pta, ptb, backend);

            Curvelist curvelist = new Curvelist(qlist, pta, ptb);

            samplingSplit(curvelist, renderhints.maxsubdivisions);

        }

        backend.endcurv();

    }
