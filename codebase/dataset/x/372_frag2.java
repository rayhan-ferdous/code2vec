                throw new IOException(e.getMessage());

            }

        }

    }



    private void copyBytes(InputStream in, OutputStream out) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        PrintWriter writer = new PrintWriter(out, true);

        String s = reader.readLine() + "\n";

        ;

        while (s != null) {

            writer.println(s);
