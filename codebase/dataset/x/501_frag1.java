    public void send(byte b[]) {

        try {

            out.write(b);

            out.flush();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
