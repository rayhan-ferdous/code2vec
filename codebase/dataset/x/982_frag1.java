    void addAuthority(Message response, Name name, Zone zone) {

        if (response.getHeader().getCount(Section.ANSWER) > 0 || zone == cache) {

            RRset nsRecords = findExactMatch(name, Type.NS, DClass.IN);

            if (nsRecords == null) nsRecords = (RRset) zone.getNS();

            Enumeration e = nsRecords.rrs();

            while (e.hasMoreElements()) {

                Record r = (Record) e.nextElement();

                if (response.findRecord(Section.ANSWER, r) == false) response.addRecord(Section.AUTHORITY, r);

            }

        } else {

            SOARecord soa = (SOARecord) zone.getSOA();

            response.addRecord(Section.AUTHORITY, soa);

        }

    }
