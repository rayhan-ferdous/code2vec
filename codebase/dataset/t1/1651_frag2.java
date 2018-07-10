        pollDirSrv.setOpenRetryPeriod(1000L * Integer.parseInt(cfg.getProperty("poll-retry-open", "60")) * 1000L);

        pollDirSrv.setDeltaLastModified(1000L * Integer.parseInt(cfg.getProperty("poll-delta-last-modified", "3")));

        String doneDirName = cfg.getProperty("poll-done-dir", "", "<none>", "");

        if (doneDirName.length() != 0) {

            File doneDir = new File(doneDirName);

            if (!doneDir.isDirectory()) {

                throw new IllegalArgumentException("Not a directory - " + doneDirName);

            }

            pollDirSrv.setDoneDir(doneDir);

        }

        return true;

    }



    private void initTLS(Configuration cfg) {

        try {

            cipherSuites = url.getCipherSuites();

            if (cipherSuites == null) {

                return;

            }

            tls = SSLContextAdapter.getInstance();

            char[] keypasswd = cfg.getProperty("tls-key-passwd", "secret").toCharArray();

            tls.setKey(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-key", "identity.p12")), keypasswd), keypasswd);

            tls.setTrust(tls.loadKeyStore(DcmSnd.class.getResource(cfg.getProperty("tls-cacerts", "cacerts.jks")), cfg.getProperty("tls-cacerts-passwd", "secret").toCharArray()));

            tls.init();

        } catch (Exception ex) {

            throw new RuntimeException("Could not initalize TLS configuration: ", ex);

        }

    }

}
