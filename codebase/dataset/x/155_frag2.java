        MediaLocator ml;

        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null) return ml;

        if (url.startsWith(File.separator)) {

            if ((ml = new MediaLocator("file:" + url)) != null) return ml;
