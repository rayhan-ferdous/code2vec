    public Lemmatizer getMostAccurateLemmatizer(final Locale locale) throws MorphologyException {

        if (locale == null) {

            throw new IllegalArgumentException("The 'loc' argument cannot be null.");

        }

        Lemmatizer component = (Lemmatizer) mostAccurateLemmatizers.get(locale);

        if (component == null) {

            throw new MorphologyException("Cannot find lemmatizer for locales : " + locale.toString());

        }

        return component;

    }
