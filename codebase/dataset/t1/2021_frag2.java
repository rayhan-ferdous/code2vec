    void addRRset(Name name, Message response, RRset rrset, byte section, int flags) {

        for (byte s = 1; s <= section; s++) if (response.findRRset(name, rrset.getType(), s)) return;

        if ((flags & FLAG_SIGONLY) == 0) {

            Iterator it = rrset.rrs();

            while (it.hasNext()) {

                Record r = (Record) it.next();

                if (r.getName().isWild() && !name.isWild()) r = r.withName(name);

                response.addRecord(r, section);

            }

        }

        if ((flags & (FLAG_SIGONLY | FLAG_DNSSECOK)) != 0) {

            Iterator it = rrset.sigs();

            while (it.hasNext()) {

                Record r = (Record) it.next();

                if (r.getName().isWild() && !name.isWild()) r = r.withName(name);

                response.addRecord(r, section);

            }

        }

    }
