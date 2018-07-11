    private InputStream getBinaryStream(MediumBlobFile blobFile) {

        InputStream is = null;

        Blob blob = blobFile.getData();

        try {

            is = blob.getBinaryStream();

        } catch (SQLException e) {

            log.error(e);

        }

        return is;

    }
