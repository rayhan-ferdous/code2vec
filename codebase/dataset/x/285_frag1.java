        for (Enumeration keysEnum = getCacheKeys().elements(); keysEnum.hasMoreElements(); ) {

            CacheKey key = (CacheKey) keysEnum.nextElement();

            if (key.getObject() == null) {

                if (key.acquireNoWait()) {

                    try {

                        if (key.getObject() == null) {
