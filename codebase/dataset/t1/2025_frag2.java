        File file = new File(EXTRACTED_KEYSTORE_LOCATION);

        if (file.isFile() == false) {

            char SEP = File.separatorChar;

            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");

            file = new File(dir, EXTRACTED_KEYSTORE_LOCATION);

            if (file.isFile() == false) {

                file = new File(dir, "cacerts");

            }

        }

        System.out.println("Carregando armaz�m " + file + "...");
