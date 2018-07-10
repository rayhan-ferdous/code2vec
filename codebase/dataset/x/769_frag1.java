        try {

            bw = new BufferedWriter(new FileWriter(new File(sorePath)));

            String str = PHPWindExtractor.getDocumentAt(url);

            Pattern pt_title = Pattern.compile("<title>(.*)</title>", Pattern.MULTILINE | Pattern.DOTALL);

            Pattern pt_listtxt = Pattern.compile("<a href=\"(.*)\" id=\"(.*)\" class=\"(.*)\">(.*)</a>", Pattern.MULTILINE);
