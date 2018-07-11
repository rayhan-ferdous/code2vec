    public static URL getBigHugeLabsURL() {

        try {

            return new URL("http://www.bighugelabs.com/flickr/scout.php");

        } catch (MalformedURLException ex) {

            throw new IllegalStateException("Should not have happened.", ex);

        }

    }
