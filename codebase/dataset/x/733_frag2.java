            if (reader != null) {

                reader = null;

            }

        }

    }



    public void HtmlFromWeb(String url) {

        setOriHtml(getDocumentAt(url));

        this.isFromWeb = true;

        setURL(url);

    }



    public boolean loadHtml(String html) {

        if (html.matches("http:.*")) {

            this.HtmlFromWeb(html);
