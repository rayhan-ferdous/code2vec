    private void initTLS(Configuration cfg) {

        try {

            cipherSuites = url.getCipherSuites();

            if (cipherSuites == null) {

                return;

            }

            tls = SSLContextAdapter.getInstance();

            char[] keypasswd = cfg.getProperty("tls-key-passwd", "secret").toCharArray();

            tls.setKey(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-key", "certificates/test_sys_1.p12")), keypasswd), keypasswd);

            tls.setTrust(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-cacerts", "certificates/mesa_certs.jks")), cfg.getProperty("tls-cacerts-passwd", "secret").toCharArray()));

            tls.init();

        } catch (Exception ex) {

            throw new RuntimeException("Could not initalize TLS configuration: ", ex);

        }

    }
