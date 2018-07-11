        String encodedData = null;

        Base64.InputStream bis = null;

        try {

            java.io.File file = new java.io.File(filename);

            byte[] buffer = new byte[Math.max((int) (file.length() * 1.4), 40)];

            int length = 0;

            int numBytes = 0;
