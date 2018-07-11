    protected void generateDiffs() {

        try {

            setupForDiff();

        } catch (IOException ioe) {

            beep(null);

            return;

        }

        if (compareB.isDirectory()) compareDirectories(compareA, compareB, compareB.getPath()); else compareFile(compareA, compareB, false, compareB.getParentFile().getPath());

        try {

            displayDiffResults();

        } catch (IOException ioe) {

        }

    }
