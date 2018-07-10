            for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements(); ) {

                try {

                    String alias = aliases.nextElement();

                    if (!keystore.isCertificateEntry(alias)) continue;

                    X509Certificate certStoreX509 = (X509Certificate) keystore.getCertificate(alias);

                    if (ocsp.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider(provider).build(certStoreX509.getPublicKey()))) return true;
