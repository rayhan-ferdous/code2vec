    Message generateReply(Message query) {

        Enumeration qds = query.getSection(Section.QUESTION);

        Record queryRecord = (Record) qds.nextElement();

        Message response = new Message();

        response.getHeader().setID(query.getHeader().getID());

        response.getHeader().setFlag(Flags.AA);

        response.getHeader().setFlag(Flags.QR);

        response.addRecord(Section.QUESTION, queryRecord);

        Name name = queryRecord.getName();

        short type = queryRecord.getType();

        Zone zone = findBestZone(name);

        if (zone == null) {

            response.getHeader().setRcode(Rcode.SERVFAIL);

        } else {

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

            addAuthority(response, zone);

            addAdditional(response);

        }

        return response;

    }
