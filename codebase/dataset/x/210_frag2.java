            writeCompressedNumber(0);

        } else {

            byte[] data;

            try {

                data = value.getBytes("UTF8");

            } catch (UnsupportedEncodingException e) {

                throw new RuntimeException(e.toString());

            }

            writeCompressedNumber(data.length);

            writeBytes(data);
