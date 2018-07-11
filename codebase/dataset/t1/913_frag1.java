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
