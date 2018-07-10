    public synchronized void putInt(String key, int data) {

        try {

            pi_bout.reset();

            writeInt(pi_dout, data);

            writeBlob(new Blob(key, pi_bout.bytes(), pi_bout.size()));

        } catch (Exception E) {

            logger.error(_base + ":" + E.getMessage());

            logger.fatal(E);

        }

    }
