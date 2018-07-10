    public void writeFile(File pFile, String pContents) {

        try {

            log.info("writing new file to: " + pFile.getAbsolutePath());

            log.debug("File already existed: " + pFile.createNewFile());

            FileWriter fw = new FileWriter(pFile);

            fw.write(pContents);

            fw.flush();

            fw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
