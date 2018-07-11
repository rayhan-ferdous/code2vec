    public ConnectionManager(AdvProperties accessCfg, int serverPort) {

        sendBufferSize = TCPConnection.DEFAULT_MAX_QUEUE;

        tcpFlush = TCPConnection.DEFAULT_TCP_FLUSH;

        this.serverPort = serverPort;

        this.accessCfg = accessCfg;

        scheduledExecutor = Executors.newScheduledThreadPool(10);

        localAddr = new PeerID(accessCfg.getPropertyBytes("access.publicKey", null), true);

        router = new Router(this);

        connector = new Connector(this);

        bitTorrentTracker = null;

        whatIsMyIP = null;

        sendLimit = new TokenBucket(0, SEND_BUCKET_SIZE);

        recLimit = new TokenBucket(0, SEND_BUCKET_SIZE);

        pinger = new Pinger(this);

        calcNetworkKey();

        (new Thread(this, "ConnectionManager")).start();

        scheduledExecutor.schedule(new Runnable() {



            public void run() {

                checkWhatIsMyIP();

            }

        }, 1, TimeUnit.SECONDS);

        try {

            if (dht == null) {

                dht = new DHT(new DatagramSocket(serverPort));

                dht.start();

            }

            dht.setSearchID(calcDHTKey());

            dht.setConnectionManager(this);

        } catch (SocketException e) {

            Logger.getLogger("").log(Level.SEVERE, "Could not start DHT", e);

        }

    }
