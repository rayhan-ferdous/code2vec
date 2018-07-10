    private byte[] showEpgWatchListMatches(HTTPurl urlData) throws Exception {

        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "epg-watchlist-matches.html");

        template.replaceAll("$title", "Matching Match List Items");

        GuideStore guide = GuideStore.getInstance();

        HashMap<String, EpgMatchList> matchLists = store.getMatchLists();
