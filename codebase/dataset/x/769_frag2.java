            googleService = ParameterEnum.googleSpeech.getDefaut();

        }

        try {

            HttpClient httpclient = new DefaultHttpClient();

            String encodeUrl = URLEncoder.encode(String.format(textToSpeech, params), "UTF-8");

            HttpGet httpget = new HttpGet(String.format(googleService, locale.substring(0, 2)) + encodeUrl);
