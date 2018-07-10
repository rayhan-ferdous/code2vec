        for (int i = 0; i < threads; i++) t[i] = new MultiUploadThread(blobs, i * iterations, iterations);

        try {

            long start = System.currentTimeMillis();

            for (int i = 0; i < threads; i++) t[i].start();

            for (int i = 0; i < threads; i++) t[i].join();

            long stop = System.currentTimeMillis();

            printTime(start, stop, iterations * threads);

        } catch (Exception e) {

            e.printStackTrace();

        }
