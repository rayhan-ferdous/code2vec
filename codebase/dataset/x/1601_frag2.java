    public static PrivateKey extractPrivateKey(byte[] berPrivateKeyInfo) throws UnrecoverableKeyException {

        ASN1DER ber = new ASN1DER();

        ByteArrayInputStream ba = new ByteArrayInputStream(berPrivateKeyInfo);

        PrivateKeyInfo pki = new PrivateKeyInfo();

        try {

            ber.decode(ba, pki);

            boolean isrsakey = true;

            try {

                String alg = pki.privateKeyAlgorithm.algorithmName().toLowerCase();

                if (alg.indexOf("dsa") >= 0) isrsakey = false;

            } catch (Throwable t) {

            }

            ba = new ByteArrayInputStream(pki.privateKey.getRaw());

            if (isrsakey) {

                com.mindbright.security.pkcs1.RSAPrivateKey rsa = new com.mindbright.security.pkcs1.RSAPrivateKey();

                ber.decode(ba, rsa);

                BigInteger n, e, d, p, q, pe, qe, u;

                n = rsa.modulus.getValue();

                e = rsa.publicExponent.getValue();

                d = rsa.privateExponent.getValue();

                p = rsa.prime1.getValue();

                q = rsa.prime2.getValue();

                pe = rsa.exponent1.getValue();

                qe = rsa.exponent2.getValue();

                u = rsa.coefficient.getValue();

                RSAPrivateCrtKeySpec prvSpec = new RSAPrivateCrtKeySpec(n, e, d, p, q, pe, qe, u);

                KeyFactory keyFact = KeyFactory.getInstance("RSA");

                return keyFact.generatePrivate(prvSpec);

            }

            BigInteger x = null;

            try {

                ASN1Integer dsax = new ASN1Integer();

                ber.decode(ba, dsax);

                x = dsax.getValue();

            } catch (Throwable t) {

            }

            if (x == null) {

                DSAyx dsayx = new DSAyx();

                ber.decode(new ByteArrayInputStream(pki.privateKey.getRaw()), dsayx);

                x = dsayx.x.getValue();

            }

            com.mindbright.security.pkcs1.DSAParams params = (com.mindbright.security.pkcs1.DSAParams) pki.privateKeyAlgorithm.parameters.getValue();

            DSAPrivateKeySpec prvSpec = new DSAPrivateKeySpec(x, params.p.getValue(), params.q.getValue(), params.g.getValue());

            KeyFactory keyFact = KeyFactory.getInstance("DSA");

            return keyFact.generatePrivate(prvSpec);

        } catch (Exception e) {

            throw new UnrecoverableKeyException(e.getMessage());

        }

    }
