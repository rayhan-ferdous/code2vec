    public void getContentIntoAFile(File localfile) throws XMLDBException {

        FileOutputStream fos = null;

        BufferedOutputStream bos = null;

        try {

            fos = new FileOutputStream(localfile);

            bos = new BufferedOutputStream(fos);

            getContentIntoAStream(bos);

        } catch (IOException ioe) {

            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, ioe.getMessage(), ioe);

        } finally {

            if (bos != null) {

                try {

                    bos.close();

                } catch (IOException ioe) {

                }

            }

            if (fos != null) {

                try {

                    fos.close();

                } catch (IOException ioe) {

                }

            }

        }

    }
