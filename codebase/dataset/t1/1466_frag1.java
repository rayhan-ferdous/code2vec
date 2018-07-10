                send(args, offset);

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

                    Dataset ds = null;
