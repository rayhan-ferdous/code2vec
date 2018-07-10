    public static boolean unzip(InputStream inputStream, String targetDir) {

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        ZipEntry zipEntry;

        FileOutputStream fileOutputStream = null;

        try {

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                if (!zipEntry.isDirectory()) {

                    targetDir = targetDir.endsWith("/") || targetDir.endsWith("\\") ? targetDir : targetDir + "/";

                    File zippedFile = new File(targetDir + zipEntry.getName());

                    FileUtilsExt.deleteQuietly(zippedFile);

                    fileOutputStream = new FileOutputStream(zippedFile);

                    int b;

                    while ((b = zipInputStream.read()) != -1) {

                        fileOutputStream.write(b);

                    }

                    fileOutputStream.close();

                }

            }

        } catch (IOException e) {

            LOGGER.error("Can't unzip to " + targetDir, e);

            return false;

        } finally {

            try {

                zipInputStream.closeEntry();

                if (fileOutputStream != null) {

                    fileOutputStream.close();

                }

            } catch (IOException e) {

                LOGGER.error("Error while closing stream.", e);

            }

        }

        return true;

    }
