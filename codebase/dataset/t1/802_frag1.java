    public void setHttpHeader() throws Exception {

        uc = (HttpURLConnection) u.openConnection();

        uc.setDoOutput(true);

        uc.setRequestProperty("Host", "www.sugarsync.com");

        uc.setRequestProperty("Connection", "keep-alive");

        uc.setRequestProperty("Referer", "https://www.sugarsync.com/");

        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");

        uc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        uc.setRequestProperty("Accept-Encoding", "html");

        uc.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

        uc.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");

        uc.setRequestProperty("Content-Type", "application/xml");

        uc.setRequestProperty("Authorization", SugarSyncAccount.getAuth_token());

        uc.setRequestMethod("POST");

        uc.setInstanceFollowRedirects(false);

    }
