                        fos = new FileOutputStream(file);

                        dest = new BufferedOutputStream(fos, BUFFER);

                        while ((count = is.read(data, 0, BUFFER)) != -1) {

                            dest.write(data, 0, count);

                        }

                        dest.flush();

                        dest.close();

                        is.close();
