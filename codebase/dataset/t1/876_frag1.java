        String sopInstUID = ds.getString(Tags.SOPInstanceUID);

        if (sopInstUID == null) {

            log.error(MessageFormat.format(messages.getString("noSOPinst"), new Object[] { file }));

            return false;

        }

        String sopClassUID = ds.getString(Tags.SOPClassUID);

        if (sopClassUID == null) {

            log.error(MessageFormat.format(messages.getString("noSOPclass"), new Object[] { file }));

            return false;

        }

        PresContext pc = null;

        Association assoc = active.getAssociation();

        if (parser.getDcmDecodeParam().encapsulated) {

            String tsuid = ds.getFileMetaInfo().getTransferSyntaxUID();

            if ((pc = assoc.getAcceptedPresContext(sopClassUID, tsuid)) == null) {

                log.error(MessageFormat.format(messages.getString("noPCStore3"), new Object[] { uidDict.lookup(sopClassUID), uidDict.lookup(tsuid), file }));

                return false;

            }

        } else if ((pc = assoc.getAcceptedPresContext(sopClassUID, UIDs.ImplicitVRLittleEndian)) == null && (pc = assoc.getAcceptedPresContext(sopClassUID, UIDs.ExplicitVRLittleEndian)) == null && (pc = assoc.getAcceptedPresContext(sopClassUID, UIDs.ExplicitVRBigEndian)) == null) {

            log.error(MessageFormat.format(messages.getString("noPCStore2"), new Object[] { uidDict.lookup(sopClassUID), file }));

            return false;

        }

        active.invoke(aFact.newDimse(pc.pcid(), oFact.newCommand().initCStoreRQ(assoc.nextMsgID(), sopClassUID, sopInstUID, priority), new MyDataSource(parser, ds, buffer)), null);

        sentBytes += parser.getStreamPosition();
