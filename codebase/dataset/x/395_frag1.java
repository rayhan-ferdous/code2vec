    public boolean validateMethodSetOutputFolder() throws Exception {

        String testRoot = TEST_MethodSetOutputFolder;

        boolean test = true;

        String actualOutputFolder = getTestFolder() + SLASH + SETOUTPUTFOLDER_FOLDER_NAME;

        String writerOutputFolder = getWriterFolder() + SLASH + SETOUTPUTFOLDER_FOLDER_NAME;

        sReferencePS.clear();

        sReferencePS.set(NAME_OutputFolder, writerOutputFolder);

        sTestPS.load(new File(actualOutputFolder, PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.equals(sReferencePS) && test;

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
