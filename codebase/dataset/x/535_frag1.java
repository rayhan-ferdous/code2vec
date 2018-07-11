    @Override

    public synchronized void close() throws SecurityException {

        try {

            lockStream.close();

            lockFile.delete();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }
