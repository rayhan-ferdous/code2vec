    public String encryptToDES(SecretKey key, String info) {

        String Algorithm = "DES";

        SecureRandom sr = new SecureRandom();

        byte[] cipherByte = null;

        try {

            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.ENCRYPT_MODE, key, sr);

            cipherByte = c1.doFinal(info.getBytes());

        } catch (Exception e) {

            e.printStackTrace();

        }

        return byte2hex(cipherByte);

    }
