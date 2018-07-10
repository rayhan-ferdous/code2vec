        } finally {

            if (fos != null) {

                try {

                    fos.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                    JdxLog.logError(ex);

                }

            }

            if (fis != null) {

                try {

                    fis.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                    JdxLog.logError(ex);

                }

            }

        }

        return ok;

    }
