    public static void appendContentToFile(byte[] content, String rootPath, String fileName) {

        File file = null;

        RandomAccessFile randomAccessFile = null;

        try {

            fileName = fileName.replace("/", "");

            rootPath = getRealFolderPath(rootPath);

            createFolder(rootPath);

            String filePath = rootPath + fileName;

            file = new File(filePath);

            randomAccessFile = new RandomAccessFile(filePath, "rw");

            if (content != null) {

                if (file.exists()) {

                    randomAccessFile.seek(randomAccessFile.length());

                } else {

                    randomAccessFile.setLength(0);

                }

                randomAccessFile.write(content);

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (randomAccessFile != null) {

                try {

                    randomAccessFile.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

    }
