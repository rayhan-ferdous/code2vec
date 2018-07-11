            md5.update(valueBeforeMD5.getBytes());

            byte array[] = md5.digest();

            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < array.length; j++) {
