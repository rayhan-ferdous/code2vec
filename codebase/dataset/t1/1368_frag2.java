    static void delete_files() {

        File f;

        int i = 0;

        boolean notFound;

        try {

            long start = System.currentTimeMillis();

            for (i = 0; i < 2000000; i++) {

                notFound = true;

                f = new File("tmp/blobs/blob" + i);

                if (f.exists()) {

                    notFound = false;

                    f.delete();

                }

                f = new File("tmp/blobs/dir" + (i % 100) + "/blob" + i);

                if (f.exists()) {

                    notFound = false;

                    f.delete();

                }

                if (notFound) break;

            }

            for (int j = 0; j < 100; j++) {

                f = new File("tmp/blobs/dir" + j + "/");

                if (f.exists()) f.delete();

            }

            long stop = System.currentTimeMillis();

            printTime(start, stop, i);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
