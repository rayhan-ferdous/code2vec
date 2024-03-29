        OPTRecord queryOPT = query.getOPT();

        if (queryOPT != null && queryOPT.getVersion() > 0) badversion = true;

        if (s != null) maxLength = 65535; else if (queryOPT != null) maxLength = Math.max(queryOPT.getPayloadSize(), 512); else maxLength = 512;

        if (queryOPT != null && (queryOPT.getFlags() & ExtendedFlags.DO) != 0) flags = FLAG_DNSSECOK;

        Message response = new Message(query.getHeader().getID());

        response.getHeader().setFlag(Flags.QR);

        if (query.getHeader().getFlag(Flags.RD)) response.getHeader().setFlag(Flags.RD);

        response.addRecord(queryRecord, Section.QUESTION);

        Name name = queryRecord.getName();

        int type = queryRecord.getType();

        int dclass = queryRecord.getDClass();

        if (type == Type.AXFR && s != null) return doAXFR(name, query, tsig, queryTSIG, s);

        if (!Type.isRR(type) && type != Type.ANY) return errorMessage(query, Rcode.NOTIMPL);

        byte rcode = addAnswer(response, name, type, dclass, 0, flags);

        if (rcode != Rcode.NOERROR && rcode != Rcode.NXDOMAIN) return errorMessage(query, rcode);

        addAdditional(response, flags);

        if (queryOPT != null) {

            int optflags = (flags == FLAG_DNSSECOK) ? ExtendedFlags.DO : 0;

            OPTRecord opt = new OPTRecord((short) 4096, rcode, (byte) 0, optflags);

            response.addRecord(opt, Section.ADDITIONAL);

        }

        response.setTSIG(tsig, Rcode.NOERROR, queryTSIG);

        return response.toWire(maxLength);

    }



    byte[] buildErrorMessage(Header header, int rcode, Record question) {

        Message response = new Message();

        response.setHeader(header);

        for (int i = 0; i < 4; i++) response.removeAllRecords(i);

        if (rcode == Rcode.SERVFAIL) response.addRecord(question, Section.QUESTION);

        header.setRcode(rcode);

        return response.toWire();

    }



    public byte[] formerrMessage(byte[] in) {

        Header header;

        try {

            header = new Header(in);

        } catch (IOException e) {

            return null;

        }

        return buildErrorMessage(header, Rcode.FORMERR, null);

    }



    public byte[] errorMessage(Message query, int rcode) {

        return buildErrorMessage(query.getHeader(), rcode, query.getQuestion());

    }



    public void TCPclient(Socket s) {

        try {

            int inLength;

            DataInputStream dataIn;

            DataOutputStream dataOut;

            byte[] in;

            InputStream is = s.getInputStream();

            dataIn = new DataInputStream(is);

            inLength = dataIn.readUnsignedShort();

            in = new byte[inLength];

            dataIn.readFully(in);

            Message query;

            byte[] response = null;

            try {

                query = new Message(in);

                response = generateReply(query, in, in.length, s);

                if (response == null) return;

            } catch (IOException e) {

                response = formerrMessage(in);

            }

            dataOut = new DataOutputStream(s.getOutputStream());

            dataOut.writeShort(response.length);

            dataOut.write(response);

        } catch (IOException e) {
