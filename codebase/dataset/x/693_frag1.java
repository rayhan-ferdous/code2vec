    public FileManager getTmp() {

        if (FM == null) {

            try {

                FM = FileManager.getDir("client");

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return FM;

    }
