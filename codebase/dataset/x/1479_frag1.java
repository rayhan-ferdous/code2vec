    public String getIp() {

        try {

            return comm.getLocalIp();

        } catch (Exception ex) {

            return "127.0.0.1";

        }

    }
