        if (model.probB != null) {

            fp.writeBytes("probB");

            for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++) fp.writeBytes(" " + model.probB[i]);

            fp.writeBytes("\n");

        }

        if (model.nSV != null) {

            fp.writeBytes("nr_sv");

            for (int i = 0; i < nr_class; i++) fp.writeBytes(" " + model.nSV[i]);

            fp.writeBytes("\n");

        }

        fp.writeBytes("SV\n");
