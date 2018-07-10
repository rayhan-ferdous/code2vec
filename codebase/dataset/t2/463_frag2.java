                byte[] data = new byte[BUFFER];

                String tempName = rootPath + entry.getName();

                if (tempName.endsWith("/")) {

                    File output = new File(tempName);

                    System.out.println("new directory " + output.getAbsoluteFile());

                    output.mkdirs();

                } else {

                    System.out.println("ouput file is " + tempName);

                    FileOutputStream fos = new FileOutputStream(tempName);

                    dest = new BufferedOutputStream(fos, BUFFER);

                    while ((count = tis.read(data, 0, BUFFER)) != -1) {

                        dest.write(data, 0, count);

                    }

                    dest.flush();

                    dest.close();

                }

                tis.close();
