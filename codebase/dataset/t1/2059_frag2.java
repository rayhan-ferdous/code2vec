                for (Enumeration aliases = keystore.aliases(); aliases.hasMoreElements(); ) {

                    try {

                        String alias = (String) aliases.nextElement();

                        if (!keystore.isCertificateEntry(alias)) continue;

                        X509Certificate certStoreX509 = (X509Certificate) keystore.getCertificate(alias);

                        if (verifyCertificate(certStoreX509, crls, calendar) != null) continue;
