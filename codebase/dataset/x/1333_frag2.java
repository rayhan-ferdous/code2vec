    public static String getCommonName(X509Certificate x509) {

        if (x509 == null) {

            return null;

        }

        String principal = x509.getSubjectDN().toString();

        int index1 = 0;

        int index2 = 0;

        index1 = principal.indexOf("CN=");

        index2 = principal.indexOf(",", index1);

        String cn = null;

        if (index2 > 0) {

            cn = principal.substring(index1 + 3, index2);

        } else {

            cn = principal.substring(index1 + 3, principal.length() - 1);

        }

        return cn;

    }
