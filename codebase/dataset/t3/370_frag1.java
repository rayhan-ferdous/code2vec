    public void blockEncrypto(Reader reader, ObjectOutputStream outputStream) {

        if (n == null && b == null && reader == null && outputStream == null) {

            System.out.println("there are some values you haven't initialized!");

            System.exit(0);

        }

        try {

            BigInteger c, m;

            int ch = reader.read();

            while (ch >= 0) {

                m = encoder.inputBlockEncrypto(ch);

                c = m.multiply((m.add(b))).mod(n);

                encoder.outputBlockEncrypto(outputStream, c);

                ch = reader.read();

            }

            outputStream.close();

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
