    public static boolean isResolvable(String host) {

        try {

            InetAddress.getByName(host);

            return true;

        } catch (UnknownHostException e) {

            return false;

        }

    }
