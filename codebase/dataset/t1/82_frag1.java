                    bytes = baos.toByteArray();

                } catch (java.io.IOException e) {

                } finally {

                    try {

                        baos.close();

                    } catch (Exception e) {

                    }

                    try {

                        gzis.close();

                    } catch (Exception e) {

                    }

                    try {

                        bais.close();

                    } catch (Exception e) {

                    }

                }

            }

        }

        return bytes;

    }



    /**

     * Attempts to decode Base64 data and deserialize a Java

     * Object within. Returns <tt>null</tt> if there was an error.

     *

     * @param encodedObject The Base64 data to decode

     * @return The decoded and deserialized object

     * @since 1.5

     */

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

            e.printStackTrace();

            obj = null;

        } catch (java.lang.ClassNotFoundException e) {

            e.printStackTrace();

            obj = null;

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
