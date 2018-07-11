    private String[] retrieveFilesToUpdate(File versionFile) throws FileNotFoundException, IOException {

        String input;

        ArrayList<String> files = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new FileReader(versionFile));

        while ((input = reader.readLine()) != null) {

            if (!input.equals(latestVersion)) {

                files.add(input);

            }

        }

        return files.toArray(new String[0]);

    }
