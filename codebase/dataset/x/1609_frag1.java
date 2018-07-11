            RRset nsRecords = findExactMatch(name, Type.NS, DClass.IN, false);

            if (nsRecords == null) {

                if (zone != null) nsRecords = zone.getNS(); else {

                    RRset[] rrsets;

                    rrsets = cache.findRecords(Name.root, Type.NS, DClass.IN);

                    if (rrsets == null) nsRecords = null; else nsRecords = rrsets[0];

                }

            }

            if (nsRecords == null) return;

            Enumeration e = nsRecords.rrs();

            while (e.hasMoreElements()) {

                Record r = (Record) e.nextElement();

                if (response.findRecord(r, Section.ANSWER) == false) response.addRecord(r, Section.AUTHORITY);

            }

        } else {
