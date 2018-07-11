    public void addPrimaryZone(String zname, String zonefile) throws IOException {

        Name origin = null;

        Cache cache = getCache(DClass.IN);

        if (zname != null) origin = Name.fromString(zname, Name.root);

        Zone newzone = new Zone(zonefile, cache, origin);

        znames.put(newzone.getOrigin(), newzone);

    }
