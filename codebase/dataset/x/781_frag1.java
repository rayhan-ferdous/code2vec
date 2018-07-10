        protected MessageDigest initialValue() {

            try {

                return MessageDigest.getInstance("MD5");

            } catch (NoSuchAlgorithmException e) {

                throw new RuntimeException(e);

            }

        }
