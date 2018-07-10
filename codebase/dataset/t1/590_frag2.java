    private int findSources(File dir, List args) {

        File[] files = dir.listFiles();

        if (files == null || files.length == 0) return 0;

        int found = 0;

        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            if (file.isDirectory()) {

                found += findSources(file, args);

            } else if (file.getName().endsWith(".java")) {

                args.add(file.toString());

                found++;

            }

        }

        return found;

    }
