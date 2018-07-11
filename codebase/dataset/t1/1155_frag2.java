    public int mconvert(File src, File dest) throws IOException {

        if (src.isFile()) {

            convert(src, dest);

            return 1;

        }

        File[] files = src.listFiles();

        if (files.length > 0 && !dest.exists()) {

            dest.mkdirs();

        }

        int count = 0;

        for (int i = 0; i < files.length; ++i) {

            count += mconvert(files[i], new File(dest, files[i].getName()));

        }

        return count;

    }
