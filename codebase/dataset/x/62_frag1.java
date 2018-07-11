    public Principal getIssuerDN() {

        if (info == null) return null;

        try {

            Principal issuer = (Principal) info.get(CertificateIssuerName.NAME + DOT + CertificateIssuerName.DN_NAME);

            return issuer;

        } catch (Exception e) {

            return null;

        }

    }
