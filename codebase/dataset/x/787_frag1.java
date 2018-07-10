        if (!file.isDirectory()) {

            for (int i = 0; i < repeatSingle; ++i) {

                if (ds != null && random) ds.putUI(Tags.SOPInstanceUID, uidGen.createUID());

                sendFile(active, file, ds);

            }

            return;
