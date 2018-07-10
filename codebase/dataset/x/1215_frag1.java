    private boolean sendDataset(ActiveAssociation active, File file, DcmParser parser, Dataset ds) throws InterruptedException, IOException {

        if (uidSuffix != null && uidSuffix.length() > 0) {

            applyUIDSuffix(ds);

        }

        doOverwrite(ds);

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

        ++sentCount;

        return true;

    }



    private void applyUIDSuffix(Dataset ds) {

        ds.putUI(Tags.StudyInstanceUID, ds.getString(Tags.StudyInstanceUID, "") + uidSuffix);

        ds.putUI(Tags.SeriesInstanceUID, ds.getString(Tags.SeriesInstanceUID, "") + uidSuffix);

        ds.putUI(Tags.SOPInstanceUID, ds.getString(Tags.SOPInstanceUID, "") + uidSuffix);

    }



    private void doOverwrite(Dataset ds) {

        for (Iterator it = overwrite.iterator(); it.hasNext(); ) {

            DcmElement el = (DcmElement) it.next();

            ds.putXX(el.tag(), el.vr(), el.getByteBuffer());

        }

    }



    private final class MyDataSource implements DataSource {



        final DcmParser parser;



        final Dataset ds;



        final byte[] buffer;



        MyDataSource(DcmParser parser, Dataset ds, byte[] buffer) {

            this.parser = parser;

            this.ds = ds;

            this.buffer = buffer;

        }



        public void writeTo(OutputStream out, String tsUID) throws IOException {

            DcmEncodeParam netParam = (DcmEncodeParam) DcmDecodeParam.valueOf(tsUID);

            if (excludePrivate) ds.excludePrivate().writeDataset(out, netParam); else ds.writeDataset(out, netParam);

            if (parser.getReadTag() == Tags.PixelData) {

                DcmDecodeParam fileParam = parser.getDcmDecodeParam();

                ds.writeHeader(out, netParam, parser.getReadTag(), parser.getReadVR(), parser.getReadLength());

                if (netParam.encapsulated) {

                    parser.parseHeader();

                    while (parser.getReadTag() == Tags.Item) {

                        ds.writeHeader(out, netParam, parser.getReadTag(), parser.getReadVR(), parser.getReadLength());

                        writeValueTo(out, false);

                        parser.parseHeader();

                    }

                    if (parser.getReadTag() != Tags.SeqDelimitationItem) {

                        throw new DcmParseException("Unexpected Tag:" + Tags.toString(parser.getReadTag()));

                    }

                    if (parser.getReadLength() != 0) {

                        throw new DcmParseException("(fffe,e0dd), Length:" + parser.getReadLength());

                    }

                    ds.writeHeader(out, netParam, Tags.SeqDelimitationItem, VRs.NONE, 0);

                } else {

                    boolean swap = fileParam.byteOrder != netParam.byteOrder && parser.getReadVR() == VRs.OW;

                    writeValueTo(out, swap);

                }

                if (truncPostPixelData) {

                    return;

                }

                ds.clear();

                try {

                    parser.parseDataset(fileParam, -1);

                } catch (IOException e) {

                    log.warn("Error reading post-pixeldata attributes:", e);

                }

                if (excludePrivate) ds.excludePrivate().writeDataset(out, netParam); else ds.writeDataset(out, netParam);

            }

        }



        private void writeValueTo(OutputStream out, boolean swap) throws IOException {

            InputStream in = parser.getInputStream();

            int len = parser.getReadLength();

            if (swap && (len & 1) != 0) {

                throw new DcmParseException("Illegal length of OW Pixel Data: " + len);

            }

            if (buffer == null) {

                if (swap) {

                    int tmp;

                    for (int i = 0; i < len; ++i, ++i) {

                        tmp = in.read();

                        out.write(in.read());

                        out.write(tmp);

                    }

                } else {

                    for (int i = 0; i < len; ++i) {

                        out.write(in.read());

                    }

                }

            } else {

                byte tmp;

                int c, remain = len;

                while (remain > 0) {

                    c = in.read(buffer, 0, Math.min(buffer.length, remain));

                    if (c == -1) {

                        throw new EOFException("EOF during read of pixel data");

                    }

                    if (swap) {

                        if ((c & 1) != 0) {

                            buffer[c++] = (byte) in.read();

                        }

                        for (int i = 0; i < c; ++i, ++i) {

                            tmp = buffer[i];

                            buffer[i] = buffer[i + 1];

                            buffer[i + 1] = tmp;

                        }

                    }

                    out.write(buffer, 0, c);

                    remain -= c;

                }

            }

            parser.setStreamPosition(parser.getStreamPosition() + len);

        }

    }



    private Socket newSocket(String host, int port) throws IOException, GeneralSecurityException {

        if (cipherSuites != null) {

            return tls.getSocketFactory(cipherSuites).createSocket(host, port);

        } else {

            return new Socket(host, port);

        }

    }



    private static void exit(String prompt, boolean error) {

        if (prompt != null) System.err.println(prompt);

        if (error) System.err.println(messages.getString("try"));

        System.exit(1);

    }



    private static String maskNull(String aet) {

        return aet != null ? aet : "DCMSND";

    }



    private final void initAssocParam(Configuration cfg, DcmURL url, boolean echo) {

        acTimeout = Integer.parseInt(cfg.getProperty("ac-timeout", "5000"));

        dimseTimeout = Integer.parseInt(cfg.getProperty("dimse-timeout", "0"));

        soCloseDelay = Integer.parseInt(cfg.getProperty("so-close-delay", "500"));

        assocRQ.setCalledAET(url.getCalledAET());

        assocRQ.setCallingAET(maskNull(url.getCallingAET()));

        assocRQ.setMaxPDULength(Integer.parseInt(cfg.getProperty("max-pdu-len", "16352")));

        assocRQ.setAsyncOpsWindow(aFact.newAsyncOpsWindow(Integer.parseInt(cfg.getProperty("max-op-invoked", "0")), 1));

        if (echo) {

            assocRQ.addPresContext(aFact.newPresContext(PCID_ECHO, UIDs.Verification, DEF_TS));

            return;

        }

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("pc.")) {

                initPresContext(Integer.parseInt(key.substring(3)), cfg.tokenize(cfg.getProperty(key), new LinkedList()));

            }

        }

    }



    private final void initPresContext(int pcid, List val) {

        Iterator it = val.iterator();

        String as = UIDs.forName((String) it.next());

        String[] tsUIDs = new String[val.size() - 1];

        for (int i = 0; i < tsUIDs.length; ++i) {

            tsUIDs[i] = UIDs.forName((String) it.next());

        }

        assocRQ.addPresContext(aFact.newPresContext(pcid, as, tsUIDs));

    }



    private void initOverwrite(Configuration cfg) {

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("set.")) {

                try {

                    overwrite.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal entry in dcmsnd.cfg - " + key + "=" + cfg.getProperty(key));

                }

            }

        }

    }



    private boolean initPollDirSrv(Configuration cfg) {
