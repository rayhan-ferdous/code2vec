    public static Encryptor getInstance() throws EncryptionException {

        if (singletonInstance == null) {

            synchronized (JavaEncryptor.class) {

                if (singletonInstance == null) {

                    singletonInstance = new JavaEncryptor();

                }

            }

        }

        return singletonInstance;

    }
