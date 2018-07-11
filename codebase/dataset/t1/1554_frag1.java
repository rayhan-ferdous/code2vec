    Message generateReply(Message query, byte[] in, int maxLength) {

        Enumeration qds = query.getSection(Section.QUESTION);

        Record queryRecord = (Record) qds.nextElement();

        TSIGRecord queryTSIG = query.getTSIG();

        TSIG tsig = null;

        if (queryTSIG != null) {

            tsig = findTSIG(queryTSIG.getName());

            if (!tsig.verify(query, in, null)) return formerrMessage(in);

        }

        Message response = new Message();

        response.getHeader().setID(query.getHeader().getID());

        response.getHeader().setFlag(Flags.AA);

        response.getHeader().setFlag(Flags.QR);

        response.addRecord(Section.QUESTION, queryRecord);

        Name name = queryRecord.getName();

        short type = queryRecord.getType();

        Zone zone = findBestZone(name);

        Hashtable nameRecords = (Hashtable) zone.findName(name);

        if (nameRecords == null) {

            response.getHeader().setRcode(Rcode.NXDOMAIN);

        } else {

            if (type == Type.ANY) {

                Enumeration e = nameRecords.elements();

                while (e.hasMoreElements()) {

                    RRset rrset = (RRset) e.nextElement();

                    addRRset(response, rrset);

                }

            } else {

                Short Type = new Short(type);

                RRset rrset = (RRset) nameRecords.get(Type);

                if (rrset != null) addRRset(response, rrset);

            }

        }

        addAuthority(response, name, zone);

        addAdditional(response);

        if (queryTSIG != null) {

            try {

                if (tsig != null) tsig.apply(response, queryTSIG);

            } catch (IOException e) {

            }

        }

        try {

            byte[] out = response.toWire();

            if (out.length > maxLength) {

                truncate(response, out.length, maxLength);

                if (tsig != null) tsig.apply(response, queryTSIG);

            }

        } catch (IOException e) {

        }

        return response;

    }
