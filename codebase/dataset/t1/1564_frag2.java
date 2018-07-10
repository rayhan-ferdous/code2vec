    public static void doVersionCheck(View view, String stableBuild, String develBuild) {

        String myBuild = jEdit.getBuild();

        String pre = myBuild.substring(6, 7);

        String variant;

        String build;

        if (pre.equals("99")) {

            variant = "stable";

            build = stableBuild;

        } else {

            variant = "devel";

            build = develBuild;

        }

        if (develBuild.compareTo(stableBuild) < 0) variant += "-nodevel";

        int retVal = GUIUtilities.confirm(view, "version-check." + variant, new String[] { MiscUtilities.buildToVersion(myBuild), MiscUtilities.buildToVersion(stableBuild), MiscUtilities.buildToVersion(develBuild) }, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (retVal == JOptionPane.YES_OPTION) jEdit.openFile(view, jEdit.getProperty("version-check.url"));

    }
