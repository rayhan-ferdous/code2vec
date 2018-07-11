            if (tsig == null || tsig.verify(query, in, length, null) != Rcode.NOERROR) return formerrMessage(in);

        }

        OPTRecord queryOPT = query.getOPT();

        if (queryOPT != null && queryOPT.getVersion() > 0) badversion = true;

        if (s != null) maxLength = 65535; else if (queryOPT != null) maxLength = Math.max(queryOPT.getPayloadSize(), 512); else maxLength = 512;

        if (queryOPT != null && (queryOPT.getFlags() & Flags.DO) != 0) flags = FLAG_DNSSECOK;

        Message response = new Message(query.getHeader().getID());

        response.getHeader().setFlag(Flags.QR);

        if (query.getHeader().getFlag(Flags.RD)) ;

        response.getHeader().setFlag(Flags.RD);

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
