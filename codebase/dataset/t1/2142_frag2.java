    public static void copyDirectory(final File sourceDir, final File destDir) throws IOException {

        if (!destDir.exists()) {

            destDir.mkdir();

        }

        final File[] children = sourceDir.listFiles();

        for (final File sourceChild : children) {

            final String name = sourceChild.getName();

            final File destChild = new File(destDir, name);

            if (sourceChild.isDirectory()) {

                copyDirectory(sourceChild, destChild);

            } else {

                copyFile(sourceChild, destChild);

            }

        }

    }
