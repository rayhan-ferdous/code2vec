    private static void set(Configuration cfg, String s) {

        int pos = s.indexOf(':');

        if (pos == -1) {

            cfg.put("set." + s, "");

        } else {

            cfg.put("set." + s.substring(0, pos), s.substring(pos + 1));

        }

    }



    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("dcmsnd", args, "", LONG_OPTS);

        Configuration cfg = new Configuration(DcmSnd.class.getResource("dcmsnd.cfg"));

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 2:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), g.getOptarg());

                    break;

                case 'P':

                    cfg.put("prior", "1");

                    break;

                case 'p':

                    cfg.put("prior", "2");

                    break;

                case 'k':

                    cfg.put("pack-pdvs", "true");

                    break;

                case 't':

                    cfg.put("trunc-post-pixeldata", "true");

                    break;

                case 'x':

                    cfg.put("exclude-private", "true");

                    break;

                case 's':

                    set(cfg, g.getOptarg());

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmSnd dcmsnd = new DcmSnd(cfg, new DcmURL(args[optind]), argc);

            dcmsnd.execute(args, optind + 1);

        } catch (IllegalArgumentException e) {

            exit(e.getMessage(), true);

        }

    }



    DcmSnd(Configuration cfg, DcmURL url, int argc) {

        this.url = url;

        this.priority = Integer.parseInt(cfg.getProperty("prior", "0"));

        this.packPDVs = "true".equalsIgnoreCase(cfg.getProperty("pack-pdvs", "false"));

        this.truncPostPixelData = "true".equalsIgnoreCase(cfg.getProperty("trunc-post-pixeldata", "false"));

        this.excludePrivate = "true".equalsIgnoreCase(cfg.getProperty("exclude-private", "false"));

        this.bufferSize = Integer.parseInt(cfg.getProperty("buf-len", "2048")) & 0xfffffffe;

        this.repeatWhole = Integer.parseInt(cfg.getProperty("repeat-assoc", "1"));

        this.repeatSingle = Integer.parseInt(cfg.getProperty("repeat-dimse", "1"));

        this.uidSuffix = cfg.getProperty("uid-suffix");

        this.mode = argc > 1 ? SEND : initPollDirSrv(cfg) ? POLL : ECHO;

        initAssocParam(cfg, url, mode == ECHO);

        initTLS(cfg);

        initOverwrite(cfg);

    }



    public void execute(String[] args, int offset) throws InterruptedException, IOException, GeneralSecurityException {

        switch(mode) {

            case ECHO:

                echo();

                break;

            case SEND:

                send(args, offset);

                break;

            case POLL:

                poll();

                break;

            default:

                throw new RuntimeException("Illegal mode: " + mode);

        }

    }



    private ActiveAssociation openAssoc() throws IOException, GeneralSecurityException {

        Association assoc = aFact.newRequestor(newSocket(url.getHost(), url.getPort()));

        assoc.setAcTimeout(acTimeout);

        assoc.setDimseTimeout(dimseTimeout);

        assoc.setSoCloseDelay(soCloseDelay);

        assoc.setPackPDVs(packPDVs);

        PDU assocAC = assoc.connect(assocRQ);

        if (!(assocAC instanceof AAssociateAC)) {

            return null;

        }

        ActiveAssociation retval = aFact.newActiveAssociation(assoc, null);

        retval.start();

        return retval;

    }



    public void echo() throws InterruptedException, IOException, GeneralSecurityException {

        long t1 = System.currentTimeMillis();

        int count = 0;

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                if (active.getAssociation().getAcceptedTransferSyntaxUID(PCID_ECHO) == null) {

                    log.error(messages.getString("noPCEcho"));

                } else for (int j = 0; j < repeatSingle; ++j, ++count) {

                    active.invoke(aFact.newDimse(PCID_ECHO, oFact.newCommand().initCEchoRQ(j)), null);

                }

                active.release(true);

            }

        }

        long dt = System.currentTimeMillis() - t1;

        log.info(MessageFormat.format(messages.getString("echoDone"), new Object[] { new Integer(count), new Long(dt) }));

    }



    public void send(String[] args, int offset) throws InterruptedException, IOException, GeneralSecurityException {

        if (bufferSize > 0) {

            buffer = new byte[bufferSize];

        }

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                for (int k = offset; k < args.length; ++k) {

                    send(active, new File(args[k]));

                }

                active.release(true);

            }

        }

        long dt = System.currentTimeMillis() - t1;

        log.info(MessageFormat.format(messages.getString("sendDone"), new Object[] { new Integer(sentCount), new Long(sentBytes), new Long(dt), new Float(sentBytes / (1.024f * dt)) }));

    }



    public void poll() {

        pollDirSrv.start(pollDir, pollPeriod);

    }



    public void openSession() throws Exception {

        activeAssociation = openAssoc();

        if (activeAssociation == null) {

            throw new IOException("Could not open association");

        }

    }



    public boolean process(File file) throws Exception {

        return sendFile(activeAssociation, file);

    }



    public void closeSession() {

        if (activeAssociation != null) {

            try {

                activeAssociation.release(true);

            } catch (Exception e) {

                log.warn("release association throws:", e);

            }

            activeAssociation = null;

        }

    }



    private void send(ActiveAssociation active, File file) throws InterruptedException, IOException {

        if (!file.isDirectory()) {

            for (int i = 0; i < repeatSingle; ++i) {

                sendFile(active, file);

            }

            return;

        }

        File[] list = file.listFiles();

        for (int i = 0; i < list.length; ++i) {

            send(active, list[i]);

        }

    }



    private boolean sendFile(ActiveAssociation active, File file) throws InterruptedException, IOException {
