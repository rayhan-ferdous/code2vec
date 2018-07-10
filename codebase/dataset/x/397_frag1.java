        byte buffer[] = new byte[2048];

        int n = 0;

        while ((n = in.read(buffer)) != -1) {

            out.write(buffer, 0, n);

        }

        out.flush();

    }
