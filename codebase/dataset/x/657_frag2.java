        opts.addOption("h", "help", false, "print this message");

        opts.addOption("V", "version", false, "print the version information and exit");

        CommandLine cl = null;

        try {

            cl = new PosixParser().parse(opts, args);

        } catch (ParseException e) {

            exit("xam: " + e.getMessage());

            throw new RuntimeException("unreachable");

        }

        if (cl.hasOption('V')) {

            Package p = XamIO.class.getPackage();

            System.out.println("xamio v" + p.getImplementationVersion());

            System.exit(0);

        }

        if (cl.hasOption('h') || !cl.hasOption('i') && !cl.hasOption('o')) {
