        public void run() {

            synchronized (tempDirsToDelete) {

                for (File dir : tempDirsToDelete) {

                    try {

                        deleteRecursive(dir);

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                }

            }

        }
