    private void readOffsetsAndLenghts(File file) {

        try {

            InputStream is = null;

            try {

                is = new FileInputStream(file);

                byte[] buffer = new byte[4096];

                int index = 0;

                for (int n; (n = is.read(buffer)) != -1; ) {

                    for (int i = 0; i < n; i++) {

                        try {

                            handleByte(buffer[i], index);

                        } catch (ArrayIndexOutOfBoundsException e) {

                            e.printStackTrace();

                            throw new ArrayIndexOutOfBoundsException("index: " + index);

                        }

                        index++;

                        if (index > 12 && index == h) {

                            break;

                        }

                    }

                }

            } finally {

                if (is != null) {

                    is.close();

                }

            }

        } catch (IOException e) {

            throw new IOError(e);

        }

    }
