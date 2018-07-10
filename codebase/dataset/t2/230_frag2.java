    private byte[] showEpgWatchListReport(HTTPurl urlData) throws Exception {

        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "epg-watchlist-report.html");

        template.replaceAll("$title", "Watch List Report");

        GuideStore guide = GuideStore.getInstance();

        HashMap<String, EpgMatchList> matchLists = store.getMatchLists();
