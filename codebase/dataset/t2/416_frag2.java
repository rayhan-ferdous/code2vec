    private void closeFile() {

        if (localFile != null) {

            try {

                localFile.close();

            } catch (Exception e) {

            }

            localFile = null;

            downloadRequest = null;

        }

    }
