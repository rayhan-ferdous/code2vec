        System.out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");

        String line = reader.readLine().trim();

        int k;

        try {

            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;

        } catch (NumberFormatException e) {

            System.out.println("KeyStore not changed");

            return;

        }

        X509Certificate cert = chain[k];

        String alias = host + "-" + (k + 1);

        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream("jssecacerts");

        ks.store(out, passphrase);

        out.close();
