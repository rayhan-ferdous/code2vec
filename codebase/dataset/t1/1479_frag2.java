    protected FCObject fromCache(FCValue id) {

        SoftReference ref = (SoftReference) _cache.get(id);

        if (ref != null) {

            FCObject obj = (FCObject) ref.get();

            if (obj == null) {

                _cache.remove(ref);

            }

            return obj;

        }

        return null;

    }
