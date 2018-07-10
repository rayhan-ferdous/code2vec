        X509Certificate cert = chain[k];

        String alias = host + "-" + (k + 1);

        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream("c:/jssecacerts");

        ks.store(out, passphrase);

        out.close();

        System.out.println();

        System.out.println(cert);

        System.out.println();

        System.out.println("Added certificate to keystore 'jssecacerts' using alias '" + alias + "'");
