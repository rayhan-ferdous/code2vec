    public void cancel() {

        try {

            transport.close();

        } catch (Exception e) {

            return;

        }

    }
