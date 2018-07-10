    public static void main(String args[]) throws Exception {

        LongOpt[] longopts = new LongOpt[8];

        longopts[0] = new LongOpt("study-uid", LongOpt.REQUIRED_ARGUMENT, null, 'S');

        longopts[1] = new LongOpt("series-uid", LongOpt.REQUIRED_ARGUMENT, null, 's');

        longopts[2] = new LongOpt("inst-uid", LongOpt.REQUIRED_ARGUMENT, null, 'i');

        longopts[3] = new LongOpt("class-uid", LongOpt.REQUIRED_ARGUMENT, null, 'c');

        longopts[4] = new LongOpt("grouplen", LongOpt.NO_ARGUMENT, null, 'g');

        longopts[5] = new LongOpt("seqlen", LongOpt.NO_ARGUMENT, null, 'q');

        longopts[6] = new LongOpt("itemlen", LongOpt.NO_ARGUMENT, null, 'm');

        longopts[7] = new LongOpt("no-fmi", LongOpt.NO_ARGUMENT, null, 'n');

        Getopt g = new Getopt("acr2dcm.jar", args, "S:s:i:c:g", longopts, true);

        Acr2Dcm acr2dcm = new Acr2Dcm();

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 'S':

                    acr2dcm.setStudyUID(g.getOptarg());

                    break;

                case 's':

                    acr2dcm.setSeriesUID(g.getOptarg());

                    break;

                case 'i':

                    acr2dcm.setInstUID(g.getOptarg());

                    break;

                case 'c':

                    acr2dcm.setClassUID(g.getOptarg());

                    break;

                case 'g':

                    acr2dcm.setSkipGroupLen(false);

                    break;

                case 'q':

                    acr2dcm.setUndefSeqLen(false);

                    break;

                case 'm':

                    acr2dcm.setUndefItemLen(false);

                    break;

                case 'n':

                    acr2dcm.setFileMetaInfo(false);

                    break;

                case '?':

                    exit("");

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc < 2) {

            exit("acr2dcm.jar: missing argument\n");

        }

        File dest = new File(args[args.length - 1]);

        long t1 = System.currentTimeMillis();

        int count = 1;

        if (dest.isDirectory()) {

            count = acr2dcm.mconvert(args, optind, dest);

        } else {

            File src = new File(args[optind]);

            if (argc > 2 || src.isDirectory()) {

                exit("acr2dcm.jar: when converting several files, " + "last argument must be a directory\n");

            }

            acr2dcm.convert(src, dest);

        }

        long t2 = System.currentTimeMillis();

        System.out.println("\nconverted " + count + " files in " + (t2 - t1) / 1000f + " s.");

    }
