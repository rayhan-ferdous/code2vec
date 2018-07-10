        Key rsaPubLoad = factory.loadFromFile(rsaPubFile, KeyFactoryImpl.RSA_KEY, true);

        Key dsaPrivLoad = factory.loadFromFile(dsaPrivFile, KeyFactoryImpl.DSA_KEY, false);

        Key rsaPrivLoad = factory.loadFromFile(rsaPrivFile, KeyFactoryImpl.RSA_KEY, false);

        HostIdentity hi = new HostIdentity(rsa.getPublic().getEncoded(), HostIdentity.RSASHA1, (short) 0, (byte) 3);
