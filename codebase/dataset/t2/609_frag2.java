        mDigest.update(uri.getBytes());

        byte d[] = mDigest.digest();

        StringBuffer hash = new StringBuffer();

        for (int i = 0; i < d.length; i++) {
