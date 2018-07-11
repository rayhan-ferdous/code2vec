    private void examineFiles(File a, File b) {

        filesAreIdentical = true;

        filesAreBinary = false;

        if (a == null || b == null) filesAreIdentical = false; else if (a.length() != b.length()) filesAreIdentical = false;

        Reader inA = null, inB = null;

        try {

            if (a != null) inA = new BufferedReader(new FileReader(a));

            if (b != null) inB = new BufferedReader(new FileReader(b));

            int charA = -2, charB = -2;

            int count = 0;

            while (true) {

                if (inA != null && charA != -1) charA = inA.read();

                if (inB != null && charB != -1) charB = inB.read();

                if (charA != charB) filesAreIdentical = false;

                if (charA == 0 || charB == 0) {

                    filesAreBinary = true;

                    break;

                }

                if ((charA == -1 && charB == -1) || (count++ > 4096 && !filesAreIdentical)) break;

            }

        } catch (IOException ioe) {

        } finally {

            try {

                if (inA != null) inA.close();

            } catch (IOException i) {

            }

            try {

                if (inB != null) inB.close();

            } catch (IOException i) {

            }

        }

    }
