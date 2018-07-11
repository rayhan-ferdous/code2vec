            long time = System.currentTimeMillis();

            int bytes = writeFile(args[i]);

            time = System.currentTimeMillis() - time;

            inputFiles++;

            if (bytes >= 0) {

                if (bytes > 0) {

                    success++;

                }

                if (!quiet) {

                    System.out.println("Wrote " + bytes + " bytes in " + (time / 60000) + "m " + ((time / 1000) % 60) + "s " + (time % 1000) + "ms (" + (time / 1000) + "s).");
