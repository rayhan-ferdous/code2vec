    public static String hmacsha1(String data, String key) {

        byte[] byteHMAC = null;

        try {

            Mac mac = Mac.getInstance("HmacSHA1");

            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");

            mac.init(spec);

            byteHMAC = mac.doFinal(data.getBytes());

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException ignore) {

        }

        String oauth = Base64.encodeToString(byteHMAC, Base64.DEFAULT);

        return oauth;

    }
