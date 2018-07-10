    private String makeSafeFilename(String input) {

        byte[] fname = input.getBytes();

        byte[] bad = new byte[] { '\\', '/' };

        byte replace = '_';

        for (int i = 0; i < fname.length; i++) {

            for (int j = 0; j < bad.length; j++) {

                if (fname[i] == bad[j]) fname[i] = replace;

            }

        }

        return new String(fname);

    }
