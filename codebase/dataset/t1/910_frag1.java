    byte addAnswer(Message response, Name name, int type, int dclass, int iterations, int flags) {

        SetResponse sr;

        byte rcode = Rcode.NOERROR;

        if (iterations > 6) return Rcode.NOERROR;

        if (type == Type.SIG || type == Type.RRSIG) {

            type = Type.ANY;

            flags |= FLAG_SIGONLY;

        }

        Zone zone = findBestZone(name);

        if (zone != null) sr = zone.findRecords(name, type); else {

            Cache cache = getCache(dclass);

            sr = cache.lookupRecords(name, type, Credibility.NORMAL);

        }

        if (sr.isUnknown()) {

            addCacheNS(response, getCache(dclass), name);

        }

        if (sr.isNXDOMAIN()) {

            response.getHeader().setRcode(Rcode.NXDOMAIN);

            if (zone != null) {

                addSOA(response, zone);

                if (iterations == 0) response.getHeader().setFlag(Flags.AA);

            }

            rcode = Rcode.NXDOMAIN;

        } else if (sr.isNXRRSET()) {

            if (zone != null) {

                addSOA(response, zone);

                if (iterations == 0) response.getHeader().setFlag(Flags.AA);

            }

        } else if (sr.isDelegation()) {

            RRset nsRecords = sr.getNS();

            addRRset(nsRecords.getName(), response, nsRecords, Section.AUTHORITY, flags);

        } else if (sr.isCNAME()) {

            CNAMERecord cname = sr.getCNAME();

            RRset rrset = new RRset(cname);

            addRRset(name, response, rrset, Section.ANSWER, flags);

            if (zone != null && iterations == 0) response.getHeader().setFlag(Flags.AA);

            rcode = addAnswer(response, cname.getTarget(), type, dclass, iterations + 1, flags);

        } else if (sr.isDNAME()) {

            DNAMERecord dname = sr.getDNAME();

            RRset rrset = new RRset(dname);

            addRRset(name, response, rrset, Section.ANSWER, flags);

            Name newname;

            try {

                newname = name.fromDNAME(dname);

            } catch (NameTooLongException e) {

                return Rcode.YXDOMAIN;

            }

            rrset = new RRset(new CNAMERecord(name, dclass, 0, newname));

            addRRset(name, response, rrset, Section.ANSWER, flags);

            if (zone != null && iterations == 0) response.getHeader().setFlag(Flags.AA);

            rcode = addAnswer(response, newname, type, dclass, iterations + 1, flags);

        } else if (sr.isSuccessful()) {

            RRset[] rrsets = sr.answers();

            for (int i = 0; i < rrsets.length; i++) addRRset(name, response, rrsets[i], Section.ANSWER, flags);

            if (zone != null) {

                addNS(response, zone, flags);

                if (iterations == 0) response.getHeader().setFlag(Flags.AA);

            } else addCacheNS(response, getCache(dclass), name);

        }

        return rcode;

    }
