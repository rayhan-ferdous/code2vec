        response.addRecord(queryRecord, Section.QUESTION);

        Name name = queryRecord.getName();

        int type = queryRecord.getType();

        int dclass = queryRecord.getDClass();

        if (type == Type.AXFR && s != null) return doAXFR(name, query, tsig, queryTSIG, s);
