    private void send(ActiveAssociation active, File file, Dataset ds) throws InterruptedException, IOException {

        if (!file.isDirectory()) {

            for (int i = 0; i < repeatSingle; ++i) {

                if (ds != null && random) ds.putUI(Tags.SOPInstanceUID, uidGen.createUID());

                sendFile(active, file, ds);

            }

            return;

        }

        File[] list = file.listFiles();

        for (int i = 0; i < list.length; ++i) {

            send(active, list[i], ds);

        }

    }
