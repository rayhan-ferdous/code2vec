    public boolean validateDirectiveInclude() throws Exception {

        String testRoot = TEST_DirectiveInclude;

        boolean test = true;

        sReferencePS.clear();

        sReferencePS.set("include0", "zero");

        sReferencePS.set("include1", "one-zero-one");

        sTestPS.load(new File(getTestFolder(), PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.contains(sReferencePS) && test;

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
