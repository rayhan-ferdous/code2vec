    public InetAddress getInetAddress() {

        try {

            return invoker.invoke(InetAddress.class, "getInetAddress", new Class<?>[] {}, new Serializable[] {});

        } catch (IOException ex) {

            Exceptions.throwNested(RuntimeException.class, ex);

            return null;

        }

    }
