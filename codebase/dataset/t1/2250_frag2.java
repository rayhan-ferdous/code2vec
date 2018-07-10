            imX = null;

            tsvd = null;

        }

        blurImage = null;

        psfImage = null;

        System.out.println("Average execution time (deblur()): " + String.format(format, av_time_deblur / 1000000000.0 / (double) NITER) + " sec");

        System.out.println("Average execution time (deblur(regParam)): " + String.format(format, av_time_deblur_regParam / 1000000000.0 / (double) NITER) + " sec");

        System.out.println("Average execution time (update()): " + String.format(format, av_time_update / 1000000000.0 / (double) NITER) + " sec");

        writeResultsToFile("FloatGTikhonov_Periodic_2D_" + threads + "_threads.txt", (double) av_time_deblur / 1000000000.0 / (double) NITER, (double) av_time_deblur_regParam / 1000000000.0 / (double) NITER, (double) av_time_update / 1000000000.0 / (double) NITER);
