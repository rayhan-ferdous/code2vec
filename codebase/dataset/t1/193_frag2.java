    public Socket(javax.net.ssl.SSLSocket socket) {

        super();

        if (null != socket) {

            this.type = TYPE_TCP;

            this.srv = null;

            this.tcp = socket;

            this.udp = null;

            this.mdp = null;

            this.shm = null;

            this.secure = true;

        } else throw new java.lang.IllegalArgumentException();

    }
