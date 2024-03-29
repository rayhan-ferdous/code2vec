    private static void cleanUpdater() {

        final File tmpDir = new File("./UPDATER_TMP");

        final File updaterFile = new File("./CoopnetUpdater.jar");

        if (tmpDir.exists() || updaterFile.exists()) {

            Logger.log(LogTypes.LOG, "Updater files queued for deletion ...");

            new Thread() {



                @Override

                public void run() {

                    try {

                        sleep(10);

                    } catch (InterruptedException ex) {

                    }

                    try {

                        if (tmpDir.exists()) {

                            Logger.log(LogTypes.LOG, "Deleting ./UPDATER_TMP recursively");

                            delete(tmpDir);

                        }

                    } catch (IOException e) {

                    }

                    try {

                        if (updaterFile.exists()) {

                            Logger.log(LogTypes.LOG, "Deleting ./CoopnetUpdater.jar");

                            delete(updaterFile);

                        }

                    } catch (IOException e) {

                    }

                }

            }.start();

        }

    }
