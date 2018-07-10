        for (int i = 0; i < list.length; ++i) {

            send(active, list[i]);

        }

    }



    private boolean sendFile(ActiveAssociation active, File file) throws InterruptedException, IOException {

        InputStream in = null;

        DcmParser parser = null;

        Dataset ds = null;

        try {

            try {

                in = new BufferedInputStream(new FileInputStream(file));
