    public static List<String> getZipFileEntryNames(File zipFile) throws IOException {

        List<String> fileList = new ArrayList<String>();

        if (zipFile == null || !zipFile.canRead()) {

            return fileList;

        }

        ZipInputStream zIn = null;

        ZipEntry zipEntry;

        final int bufSize = 1024 * 16;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);

        zIn = new ZipInputStream(in);

        try {

            while ((zipEntry = zIn.getNextEntry()) != null) {

                if (!zipEntry.isDirectory()) {

                    String zipEntryName = zipEntry.getName();

                    fileList.add(zipEntryName);

                }

                zIn.closeEntry();

            }

        } catch (IOException ex) {

            zIn.close();

            throw ex;

        } finally {

            zIn.close();

        }

        return fileList;

    }
