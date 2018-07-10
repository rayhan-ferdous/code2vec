    @Override

    public void close() {

        try {

            super.close();

            fileChannel.close();

        } catch (Exception ex) {

        }

    }
