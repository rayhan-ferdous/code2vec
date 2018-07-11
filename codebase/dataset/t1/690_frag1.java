        System.setProperty("org.marc4j.marc.MarcFactory", "org.solrmarc.marcoverride.NoSortMarcFactoryImpl");

        String testDataParentPath = System.getProperty("test.data.path");

        String testConfigFile = System.getProperty("test.config.file");

        if (testDataParentPath == null) fail("property test.data.path must be defined for the tests to run");

        if (testConfigFile == null) fail("property test.config.file be defined for this test to run");

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();

        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcSorter", "main", null, out1, new String[] { testDataParentPath + "/url_test_recs.mrc" });
