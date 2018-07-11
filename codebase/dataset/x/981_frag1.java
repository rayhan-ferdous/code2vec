    public static final synchronized CrawlCacheManager getCrawlCacheManager() throws SearchLibException {

        if (crawlCacheManager != null) return crawlCacheManager;

        try {

            return crawlCacheManager = new CrawlCacheManager(StartStopListener.OPENSEARCHSERVER_DATA_FILE);

        } catch (InvalidPropertiesFormatException e) {

            throw new SearchLibException(e);

        } catch (IOException e) {

            throw new SearchLibException(e);

        } catch (InstantiationException e) {

            throw new SearchLibException(e);

        } catch (IllegalAccessException e) {

            throw new SearchLibException(e);

        }

    }
