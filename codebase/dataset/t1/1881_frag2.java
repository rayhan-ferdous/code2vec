    public RRset findExactMatch(Name name, int type, int dclass, boolean glue) {

        Zone zone = findBestZone(name);

        if (zone != null) return zone.findExactMatch(name, type); else {

            RRset[] rrsets;

            Cache cache = getCache(dclass);

            if (glue) rrsets = cache.findAnyRecords(name, type); else rrsets = cache.findRecords(name, type);

            if (rrsets == null) return null; else return rrsets[0];

        }

    }
