    private void initTLS(Configuration cfg) {

        try {

            cipherSuites = url.getCipherSuites();

            if (cipherSuites == null) {

                return;

            }

            tls = SSLContextAdapter.getInstance();

            char[] keypasswd = cfg.getProperty("tls-key-passwd", "passwd").toCharArray();

            tls.setKey(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-key", "identity.p12")), keypasswd), keypasswd);

            tls.setTrust(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-cacerts", "cacerts.jks")), cfg.getProperty("tls-cacerts-passwd", "passwd").toCharArray()));

            tls.init();

        } catch (Exception ex) {

            throw new RuntimeException("Could not initalize TLS configuration: ", ex);

        }

    }
