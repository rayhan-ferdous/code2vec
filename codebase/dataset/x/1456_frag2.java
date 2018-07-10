        return SPEEX_NAME;

    }



    @Override

    public void parse(InputSource input, NormaliserResults results, boolean convertOnly) throws IOException, SAXException {

        try {

            if (!(input instanceof XenaInputSource)) {

                throw new XenaException("Can only normalise XenaInputSource objects.");

            }

            XenaInputSource xis = (XenaInputSource) input;

            SpeexAudioFileReader speexReader = new SpeexAudioFileReader();

            AudioInputStream audioIS;

            if (xis.getFile() == null) {

                audioIS = speexReader.getAudioInputStream(xis.getByteStream());
