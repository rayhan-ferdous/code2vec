    public static void copyDir(File sourceDirectory, File destinationDirectory) {

        if (sourceDirectory.isDirectory()) {

            if (!destinationDirectory.exists()) {

                destinationDirectory.mkdir();

            }

            String[] children = sourceDirectory.list();

            for (int i = 0; i < children.length; i++) {

                copyDir(new File(sourceDirectory, children[i]), new File(destinationDirectory, children[i]));

            }

        } else {

            try {

                copyFile(sourceDirectory, destinationDirectory);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }
