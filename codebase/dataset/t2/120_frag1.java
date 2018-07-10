    private void bounceAll() {

        File[] spoolFiles = spoolPath.listFiles();

        try {

            for (int i = 0; i < spoolFiles.length; i++) {

                try {

                    if (spoolFiles[i].isFile()) {

                        this.bounceFile(new File(spoolFiles[i].getName()));

                    }

                } catch (NullPointerException e) {
