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
