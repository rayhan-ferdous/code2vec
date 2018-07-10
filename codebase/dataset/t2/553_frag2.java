    public Cache getCache(short dclass) {

        Cache c = (Cache) caches.get(new Short(dclass));

        if (c == null) {

            c = new Cache(dclass);

            caches.put(new Short(dclass), c);

        }

        return c;

    }
