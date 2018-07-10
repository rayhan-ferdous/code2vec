    private void init(RequestProcessor processor, String user, String apiKey, String url) throws Exception {

        this.processor = processor;

        this.user = user;

        this.apiKey = apiKey;

        if (StringUtils.isNotEmpty(url)) {

            this.url = url;

        } else {

            this.url = "https://api.cybersms.ru/";

        }

        this.plainKey = this.url.startsWith("https");

        this.processor.init(this.url);

        this.gson = (new GsonBuilder()).registerTypeAdapter(Date.class, new JsonDateConverter()).create();

        this.dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);

        this.dateTimeFormat.setTimeZone(TIME_ZONE_UTC);

        this.dateFormat.setTimeZone(TIME_ZONE_UTC);

    }
