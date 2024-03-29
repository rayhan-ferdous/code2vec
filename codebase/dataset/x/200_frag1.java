    public void execute(final String[] pArguments) throws InvalidParameterException, UtilisationFichierInterditeException, IOException {

        if (pArguments.length != 3) {

            throw new InvalidParameterException();

        }

        final File lFichierDonneesDestinataires = new File(pArguments[0]);

        aFichierLettreType = new File(pArguments[1]);

        aRepertoireSortie = new File(pArguments[2]);

        FileUtils.verifAutorisationLectureFichier(pArguments[0], lFichierDonneesDestinataires, 0, true);

        FileUtils.verifAutorisationLectureFichier(pArguments[1], aFichierLettreType, 1, true);

        FileUtils.verifAutorisationEcritureFichier(pArguments[2], aRepertoireSortie, 2, false);

        aDonneesProgramme = new DonneesProgramme();

        aDonneesProgramme.initialisation(lFichierDonneesDestinataires);

        executeClonage();

    }
