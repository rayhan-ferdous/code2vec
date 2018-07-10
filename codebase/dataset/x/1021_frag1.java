                fft3.complexForward(x);

            }

            double av_time = 0;

            long elapsedTime = 0;

            for (int j = 0; j < niter; j++) {

                IoUtils.fillMatrix_3D(N, N, 2 * N, x);

                elapsedTime = System.nanoTime();

                fft3.complexForward(x);

                elapsedTime = System.nanoTime() - elapsedTime;

                av_time = av_time + elapsedTime;

            }

            times[i] = (double) av_time / 1000000.0 / (double) niter;

            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");

            x = null;
