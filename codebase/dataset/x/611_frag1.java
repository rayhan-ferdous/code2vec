    public static void writeArrayAsPropertiesToFile(File file, Object[] data) throws IOException {

        PrintWriter pw = null;

        FileWriter fw = null;

        fw = new FileWriter(file);

        pw = new PrintWriter(fw);

        for (int i = 0; i < data.length; i++) {

            Object[] d = (Object[]) data[i];

            pw.print(d[0]);

            pw.print(":");

            pw.println(d[1]);

        }

        pw.flush();

        fw.close();

    }
