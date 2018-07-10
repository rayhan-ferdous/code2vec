    public static void makeDocLocal(HTMLDocument doc, String dir, int mode) throws IOException {

        ElementIterator i = new ElementIterator(doc);

        Element el;

        SavingCache cache = new SavingCache(doc);

        while ((el = i.next()) != null) {

            HTML.Tag eltag = (HTML.Tag) el.getAttributes().getAttribute(StyleConstants.NameAttribute);

            if (HTML.Tag.IMG.equals(eltag)) {

                makeImgLocal(doc, dir, cache, el);

            }

        }

    }
