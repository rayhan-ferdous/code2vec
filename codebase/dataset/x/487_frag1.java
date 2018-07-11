                if (active.getAssociation().getAcceptedTransferSyntaxUID(PCID_ECHO) == null) {

                    log.error(messages.getString("noPCEcho"));

                } else for (int j = 0; j < repeatSingle; ++j, ++count) {

                    active.invoke(aFact.newDimse(PCID_ECHO, oFact.newCommand().initCEchoRQ(j)), null);
