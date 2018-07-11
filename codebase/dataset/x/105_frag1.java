    public BigInteger generateSecret(PrivateKey privKey, PublicKey pubKey) throws OtrCryptoException {

        try {

            KeyAgreement ka = KeyAgreement.getInstance("DH");

            ka.init(privKey);

            ka.doPhase(pubKey, true);

            byte[] sb = ka.generateSecret();

            BigInteger s = new BigInteger(1, sb);

            return s;

        } catch (Exception e) {

            throw new OtrCryptoException(e);

        }

    }
