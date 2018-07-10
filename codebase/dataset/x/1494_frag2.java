    private CertificateFactory getCertificateFactory() {

        try {

            return CertificateFactory.getInstance("X.509", "BC");

        } catch (NoSuchProviderException nspe) {

            LOG.error("Error creating certificate factory", nspe);

        } catch (CertificateException ce) {

            LOG.error("Error creating certificate factory", ce);

        }

        return null;

    }
