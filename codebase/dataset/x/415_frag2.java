    public void purge() throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        long t1 = System.currentTimeMillis();

        long len1 = dirFile.length();

        int count = 0;

        try {

            count = doPurge(writer);

        } finally {

            writer.close();

        }

        long t2 = System.currentTimeMillis();

        long len2 = dirFile.length();

        System.out.println(MessageFormat.format(messages.getString("purgeDone"), new Object[] { String.valueOf(count), String.valueOf((t2 - t1) / 1000f) }));
