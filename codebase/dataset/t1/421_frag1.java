        Map<Integer, byte[]> dataGroups1 = new LinkedHashMap<Integer, byte[]>();

        dataGroups1.put(1, digestHelper("Dummy Value 1".getBytes(), "SHA256"));

        dataGroups1.put(2, digestHelper("Dummy Value 2".getBytes(), "SHA256"));

        signHelper(WORKER1, 12, dataGroups1, false, "SHA256", "SHA256withRSA");

        Map<Integer, byte[]> dataGroups2 = new LinkedHashMap<Integer, byte[]>();

        dataGroups2.put(3, digestHelper("Dummy Value 3".getBytes(), "SHA256"));

        dataGroups2.put(7, digestHelper("Dummy Value 4".getBytes(), "SHA256"));

        dataGroups2.put(8, digestHelper("Dummy Value 5".getBytes(), "SHA256"));

        dataGroups2.put(13, digestHelper("Dummy Value 6".getBytes(), "SHA256"));

        signHelper(WORKER1, 13, dataGroups2, false, "SHA256", "SHA256withRSA");

        Map<Integer, byte[]> dataGroups3 = new LinkedHashMap<Integer, byte[]>();

        dataGroups3.put(1, digestHelper("Dummy Value 7".getBytes(), "SHA512"));

        dataGroups3.put(2, digestHelper("Dummy Value 8".getBytes(), "SHA512"));

        signHelper(WORKER2, 14, dataGroups3, false, "SHA512", "SHA512withRSA");

        Map<Integer, byte[]> dataGroups4 = new LinkedHashMap<Integer, byte[]>();
