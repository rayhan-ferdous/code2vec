        sReferencePS.set(NAME_OutputFolder, getWriterFolder());

        sTestPS.load(new File(getTestFolder(), PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.equals(sReferencePS) && test;

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
