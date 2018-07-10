    public static final void initLang(String lang, Shell shell, boolean redraw) {

        for (Locale l : NLSMessages.supportedLocales) {

            if (lang.equals(l.getLanguage())) {

                NLSMessages.init(NLSMessages.supportedLocales.indexOf(l));

                EdutexProperties properties = new EdutexProperties("edutex");

                properties.setProperty("lang", l.getLanguage(), true);

                break;

            }

        }

    }
