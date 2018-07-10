                    byte[] buf = new byte[1024];

                    int readCant = 0;

                    while ((readCant = fis.read(buf)) != -1) {

                        fos.write(buf, 0, readCant);

                    }

                    fis.close();

                    fos.close();
