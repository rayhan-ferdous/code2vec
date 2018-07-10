    public static final void loadProperties(final Properties props, final InputStream inStream) {

        if (null == inStream) return;

        try {

            props.load(inStream);

        } catch (final Exception e) {

            e.printStackTrace();

        } finally {

            closeIO(inStream);

        }

    }
