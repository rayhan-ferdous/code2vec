        } finally {

            if (fos != null) {

                try {

                    fos.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                    JdxLog.logError(ex);

                }

            }

            if (is != null) {

                try {

                    is.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                    JdxLog.logError(ex);

                }

            }

        }

        return ok;

    }
