    public java.io.Serializable ReadFile(String fileName) throws Exception {

        FileInputStream in;

        AlgorithmParameterSpec aps;

        java.io.Serializable retObj;

        try {

            in = new FileInputStream(fileName);

            in.read(salt);

            KeySpec ks = new PBEKeySpec(filePasswd.toCharArray());

            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);

            key = skf.generateSecret(ks);

            aps = new PBEParameterSpec(salt, iterations);

            cipher = Cipher.getInstance(algorithm);

            cipher.init(Cipher.DECRYPT_MODE, key, aps);

            ObjectInputStream s = new ObjectInputStream(in);

            SealedObject so = (SealedObject) s.readObject();

            retObj = (java.io.Serializable) so.getObject(cipher);

            in.close();

        } catch (Exception e) {

            Log.out("fileName=" + fileName);

            Log.out("algorithm=" + algorithm);

            Log.out(e);

            throw e;

        }

        return retObj;

    }
