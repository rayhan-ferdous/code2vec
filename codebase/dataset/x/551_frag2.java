    private static CommandLine parse(String[] args) {

        Options opts = new Options();

        OptionBuilder.withArgName("name");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("set device name, use DCMRCV by default");

        opts.addOption(OptionBuilder.create("device"));

        OptionBuilder.withArgName("NULL|3DES|AES");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("enable TLS connection without, 3DES or AES encryption");

        opts.addOption(OptionBuilder.create("tls"));

        OptionGroup tlsProtocol = new OptionGroup();

        tlsProtocol.addOption(new Option("tls1", "disable the use of SSLv3 and SSLv2 for TLS connections"));

        tlsProtocol.addOption(new Option("ssl3", "disable the use of TLSv1 and SSLv2 for TLS connections"));

        tlsProtocol.addOption(new Option("no_tls1", "disable the use of TLSv1 for TLS connections"));

        tlsProtocol.addOption(new Option("no_ssl3", "disable the use of SSLv3 for TLS connections"));

        tlsProtocol.addOption(new Option("no_ssl2", "disable the use of SSLv2 for TLS connections"));

        opts.addOptionGroup(tlsProtocol);

        opts.addOption("noclientauth", false, "disable client authentification for TLS");

        OptionBuilder.withArgName("file|url");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("file path or URL of P12 or JKS keystore, resource:tls/test_sys_2.p12 by default");

        opts.addOption(OptionBuilder.create("keystore"));

        OptionBuilder.withArgName("password");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("password for keystore file, 'secret' by default");

        opts.addOption(OptionBuilder.create("keystorepw"));

        OptionBuilder.withArgName("password");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("password for accessing the key in the keystore, keystore password by default");

        opts.addOption(OptionBuilder.create("keypw"));

        OptionBuilder.withArgName("file|url");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("file path or URL of JKS truststore, resource:tls/mesa_certs.jks by default");

        opts.addOption(OptionBuilder.create("truststore"));

        OptionBuilder.withArgName("password");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("password for truststore file, 'secret' by default");

        opts.addOption(OptionBuilder.create("truststorepw"));

        OptionBuilder.withArgName("dir");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("store received objects into files in specified directory <dir>." + " Do not store received objects by default.");

        opts.addOption(OptionBuilder.create("dest"));

        OptionBuilder.withArgName("file|url");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("file path or URL of properties for mapping Calling AETs to " + "sub-directories of the storage directory specified by " + "-dest, to separate the storage location dependend on " + "Calling AETs.");

        opts.addOption(OptionBuilder.create("calling2dir"));

        OptionBuilder.withArgName("file|url");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("file path or URL of properties for mapping Called AETs to " + "sub-directories of the storage directory specified by " + "-dest, to separate the storage location dependend on " + "Called AETs.");

        opts.addOption(OptionBuilder.create("called2dir"));

        OptionBuilder.withArgName("sub-dir");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("storage sub-directory used for Calling AETs for which no " + " mapping is defined by properties specified by " + "-calling2dir, 'OTHER' by default.");

        opts.addOption(OptionBuilder.create("callingdefdir"));

        OptionBuilder.withArgName("sub-dir");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("storage sub-directory used for Called AETs for which no " + " mapping is defined by properties specified by " + "-called2dir, 'OTHER' by default.");

        opts.addOption(OptionBuilder.create("calleddefdir"));

        OptionBuilder.withArgName("dir");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("register stored objects in cache journal files in specified directory <dir>." + " Do not register stored objects by default.");

        opts.addOption(OptionBuilder.create("journal"));

        OptionBuilder.withArgName("pattern");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("cache journal file path, with " + "'yyyy' will be replaced by the current year, " + "'MM' by the current month, 'dd' by the current date, " + "'HH' by the current hour and 'mm' by the current minute. " + "'yyyy/MM/dd/HH/mm' by default.");

        opts.addOption(OptionBuilder.create("journalfilepath"));

        opts.addOption("defts", false, "accept only default transfer syntax.");

        opts.addOption("bigendian", false, "accept also Explict VR Big Endian transfer syntax.");

        opts.addOption("native", false, "accept only transfer syntax with uncompressed pixel data.");

        OptionGroup scRetrieveAET = new OptionGroup();

        OptionBuilder.withArgName("aet");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("Retrieve AE Title included in Storage Commitment " + "N-EVENT-REPORT in items of the Referenced SOP Sequence.");

        scRetrieveAET.addOption(OptionBuilder.create("scretraets"));

        OptionBuilder.withArgName("aet");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("Retrieve AE Title included in Storage Commitment " + "N-EVENT-REPORT outside of the Referenced SOP Sequence.");

        scRetrieveAET.addOption(OptionBuilder.create("scretraet"));

        opts.addOptionGroup(scRetrieveAET);

        opts.addOption("screusefrom", false, "attempt to issue the Storage Commitment N-EVENT-REPORT on " + "the same Association on which the N-ACTION operation was " + "performed; use different Association for N-EVENT-REPORT by " + "default.");

        opts.addOption("screuseto", false, "attempt to issue the Storage Commitment N-EVENT-REPORT on " + "previous initiated Association to the Storage Commitment SCU; " + "initiate new Association for N-EVENT-REPORT by default.");

        OptionBuilder.withArgName("port");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("port of Storage Commitment SCU to connect to issue " + "N-EVENT-REPORT on different Association; 104 by default.");

        opts.addOption(OptionBuilder.create("scport"));

        OptionBuilder.withDescription("create a dmz containing the recieved files; off by default.");

        opts.addOption(OptionBuilder.create("zip"));

        OptionBuilder.withDescription("e-mail a dmz containing the recieved files; off by default.");

        opts.addOption(OptionBuilder.create("zipsend"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("interval in ms to wait for further STORE requests before " + "finalising the zip file; 5000 by default.");

        opts.addOption(OptionBuilder.create("ziptimeout"));

        OptionBuilder.withArgName("email");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("e-mail address to send recieved files to");

        opts.addOption(OptionBuilder.create("emailto"));

        OptionBuilder.withArgName("email");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("e-mail address from which to send files");

        opts.addOption(OptionBuilder.create("emailfrom"));

        OptionBuilder.withArgName("host");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("SMTP server used to send e-mails");

        opts.addOption(OptionBuilder.create("emailsmtpserver"));

        OptionBuilder.withArgName("username");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("username used to authenticate with the SMTP server");

        opts.addOption(OptionBuilder.create("emailuser"));

        OptionBuilder.withArgName("password");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("password used to authenticate with the SMTP server");

        opts.addOption(OptionBuilder.create("emailpassword"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("delay in ms for N-EVENT-REPORT-RQ to Storage Commitment SCU, " + "1s by default");

        opts.addOption(OptionBuilder.create("scdelay"));

        OptionBuilder.withArgName("retry");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("number of retries to issue N-EVENT-REPORT-RQ to Storage " + "Commitment SCU, 0 by default");

        opts.addOption(OptionBuilder.create("scretry"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("interval im ms between retries to issue N-EVENT-REPORT-RQ to" + "Storage Commitment SCU, 60s by default");

        opts.addOption(OptionBuilder.create("scretryperiod"));

        OptionBuilder.withArgName("maxops");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("maximum number of outstanding operations performed " + "asynchronously, unlimited by default.");

        opts.addOption(OptionBuilder.create("async"));

        opts.addOption("pdv1", false, "send only one PDV in one P-Data-TF PDU, " + "pack command and data PDV in one P-DATA-TF PDU by default.");

        opts.addOption("tcpdelay", false, "set TCP_NODELAY socket option to false, true by default");

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for TCP connect, no timeout by default");

        opts.addOption(OptionBuilder.create("connectTO"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for receiving DIMSE-RSP, 10s by default");

        opts.addOption(OptionBuilder.create("rspTO"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for receiving A-ASSOCIATE-AC, 5s by default");

        opts.addOption(OptionBuilder.create("acceptTO"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("delay in ms for Socket close after sending A-ABORT, 50ms by default");

        opts.addOption(OptionBuilder.create("soclosedelay"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("delay in ms for DIMSE-RSP; useful for testing asynchronous mode");

        opts.addOption(OptionBuilder.create("rspdelay"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for receiving -ASSOCIATE-RQ, 5s by default");

        opts.addOption(OptionBuilder.create("requestTO"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for receiving A-RELEASE-RP, 5s by default");

        opts.addOption(OptionBuilder.create("releaseTO"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("period in ms to check for outstanding DIMSE-RSP, 10s by default");

        opts.addOption(OptionBuilder.create("reaper"));

        OptionBuilder.withArgName("ms");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("timeout in ms for receiving DIMSE-RQ, 60s by default");

        opts.addOption(OptionBuilder.create("idleTO"));

        OptionBuilder.withArgName("KB");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("maximal length in KB of received P-DATA-TF PDUs, 16KB by default");

        opts.addOption(OptionBuilder.create("rcvpdulen"));

        OptionBuilder.withArgName("KB");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("maximal length in KB of sent P-DATA-TF PDUs, 16KB by default");

        opts.addOption(OptionBuilder.create("sndpdulen"));

        OptionBuilder.withArgName("KB");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("set SO_RCVBUF socket option to specified value in KB");

        opts.addOption(OptionBuilder.create("sorcvbuf"));

        OptionBuilder.withArgName("KB");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("set SO_SNDBUF socket option to specified value in KB");

        opts.addOption(OptionBuilder.create("sosndbuf"));

        OptionBuilder.withArgName("KB");

        OptionBuilder.hasArg();

        OptionBuilder.withDescription("minimal buffer size to write received object to file, 1KB by default");

        opts.addOption(OptionBuilder.create("bufsize"));

        opts.addOption("h", "help", false, "print this message");

        opts.addOption("V", "version", false, "print the version information and exit");

        CommandLine cl = null;

        try {

            cl = new GnuParser().parse(opts, args);

        } catch (ParseException e) {

            exit("dcmrcv: " + e.getMessage());

            throw new RuntimeException("unreachable");

        }

        if (cl.hasOption("V")) {

            Package p = DcmRcv.class.getPackage();

            System.out.println("dcmrcv v" + p.getImplementationVersion());

            System.exit(0);

        }

        if (cl.hasOption("h") || cl.getArgList().size() == 0) {

            HelpFormatter formatter = new HelpFormatter();

            formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);

            System.exit(0);

        }

        return cl;

    }
