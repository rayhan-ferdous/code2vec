        byte[] decodedData = null;

        Base64.InputStream bis = null;

        try {

            java.io.File file = new java.io.File(filename);

            byte[] buffer = null;

            int length = 0;

            int numBytes = 0;
