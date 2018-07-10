    public void remove(String[] args, int off, boolean delFiles) throws IOException {

        long t1 = System.currentTimeMillis();

        int[] counter = new int[2];

        DirWriter w = fact.newDirWriter(dirFile, encodeParam());

        try {

            for (int i = off; i < args.length; ++i) {

                addFileIDs(w, new File(args[i]));

            }

            doRemove(w, counter, delFiles);

        } finally {

            w.close();

        }

        long t2 = System.currentTimeMillis();

        System.out.println(MessageFormat.format(messages.getString("removeDone"), new Object[] { String.valueOf(counter[1]), String.valueOf(counter[0]), String.valueOf((t2 - t1) / 1000f) }));

    }
