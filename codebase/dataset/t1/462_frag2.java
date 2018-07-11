    public static Object decodeToObject(String encodedObject) {

        byte[] objBytes = decode(encodedObject);

        java.io.ByteArrayInputStream bais = null;

        java.io.ObjectInputStream ois = null;

        Object obj = null;

        try {

            bais = new java.io.ByteArrayInputStream(objBytes);

            ois = new java.io.ObjectInputStream(bais);

            obj = ois.readObject();

        } catch (java.io.IOException e) {

            throw new Base64Exception("IOException while decoding...", e);

        } catch (java.lang.ClassNotFoundException e) {

            throw new Base64Exception("ClassNotFoundException while decoding...", e);

        } finally {

            try {

                bais.close();

            } catch (Exception e) {

            }

            try {

                ois.close();

            } catch (Exception e) {

            }

        }

        return obj;

    }
