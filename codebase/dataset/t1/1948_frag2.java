        longopts[5] = new LongOpt("seqlen", LongOpt.NO_ARGUMENT, null, 'q');

        longopts[6] = new LongOpt("itemlen", LongOpt.NO_ARGUMENT, null, 'm');

        longopts[7] = new LongOpt("no-fmi", LongOpt.NO_ARGUMENT, null, 'n');

        Getopt g = new Getopt("acr2dcm.jar", args, "S:s:i:c:g", longopts, true);
