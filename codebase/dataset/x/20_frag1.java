    public InetAddress getBindingServerInet() {

        try {

            return (InetAddress) readObject("bindingServerInet");

        } catch (Exception e) {

            return null;

        }

    }
