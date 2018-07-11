    public static File urlToFile(URL url) {

        if (isFileURL(url)) {

            String surl = url.toString();

            surl = StringUtils.replace(surl, "file://", "");

            surl = StringUtils.replace(surl, "file:/", "");

            surl = StringUtils.urlDecodeFilename(surl.toCharArray());

            File f = new File(surl);

            return f;

        } else {

            return null;

        }

    }
