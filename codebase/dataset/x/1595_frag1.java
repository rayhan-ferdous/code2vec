    public void close() {

        try {

            logStream.close();

        } catch (Exception e) {

            System.out.println("Problem with close e=" + e);

        }

    }
