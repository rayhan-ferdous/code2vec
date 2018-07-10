    public void blockEncrypto(Reader reader, OutputStream outputStream) {

        forEncryption = true;

        BigInteger bi;

        if (e == null && n == null && reader == null && outputStream == null) {

            System.out.println("there are some values you haven't initialized!");

            System.exit(0);

        }

        try {

            int ch = reader.read();

            while (ch >= 0) {

                bi = encoder.inputBlockEncrypto(ch);

                bi = bi.modPow(e, n);

                encoder.outputBlockEncrypto(outputStream, bi);

                ch = reader.read();

            }

            outputStream.close();

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
