    private void getRandomGUID(boolean secure) {

        MessageDigest md5 = null;

        StringBuffer sbValueBeforeMD5 = new StringBuffer();

        try {

            md5 = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {

            System.out.println("Error: " + e);

        }

        try {

            long time = System.currentTimeMillis();

            long rand = 0;

            if (secure) {

                rand = mySecureRand.nextLong();

            } else {

                rand = myRand.nextLong();

            }

            sbValueBeforeMD5.append(s_id);
