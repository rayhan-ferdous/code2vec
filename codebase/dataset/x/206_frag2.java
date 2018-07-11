    public static Boolean writeFile(String sFilename, String sContent) {

        try {

            OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(sFilename), "UTF-8");

            oWriter.write(sContent);

            oWriter.close();

        } catch (IOException oException) {

            return false;

        }

        return true;

    }
