    public boolean validateVersion_0_1() throws Exception {

        String testRoot = TEST_Version_0_1;

        boolean test = true;

        sReferencePS.clear();

        sReferencePS.set("body", "bodyCheck");

        sReferencePS.set("init", "initCheck");

        sReferencePS.set("support", "supportCheck");

        sTestPS.load(new File(getTestFolder(), PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.contains(sReferencePS) && test;

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
