    public ArrayList<News> getNewsList(int count, int filter) throws ConsolewarsAPIException {

        ArrayList<News> news = new ArrayList<News>();

        String apiname = "getnewslist";

        URLCreator newslistURL = new URLCreator(BASEURL + apiname + ".php");

        newslistURL.addArgument("apitoken", APIKey);

        newslistURL.addArgument("count", count);

        if (filter != BLANK_ARGUMENT) newslistURL.addArgument("filter", filter);

        SAXNewsParser parser = new SAXNewsParser(newslistURL.toString());

        news = parser.parseDocument();

        parser = null;

        return removeNewsTeaser(news);

    }
