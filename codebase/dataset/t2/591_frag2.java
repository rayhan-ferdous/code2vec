        for (int i = 0; i < numTri; i++) {

            String name = est.getString();

            String left = est.getString();

            String right = est.getString();

            String position = est.getString();

            String attribute = est.getString();

            int tmat = est.getInt("tmat");

            int[] stid = new int[numStatePerHMM - 1];

            for (int j = 0; j < numStatePerHMM - 1; j++) {

                stid[j] = est.getInt("j");

                assert stid[j] >= numContextIndependentTiedState && stid[j] < numTiedState;
