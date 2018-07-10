                    FileOutputStream oOutput = new FileOutputStream(sDestFN);

                    oDestination = new BufferedOutputStream(oOutput, BUFFER_SIZE);

                    while ((iCount = oInput.read(aData, 0, BUFFER_SIZE)) != -1) {

                        oDestination.write(aData, 0, iCount);

                    }

                    oDestination.flush();

                    oDestination.close();

                }
