                copyDir(fs("bin/webinstall/"), dirName + File.separatorChar);

                Vector fileList = myConfig.getFiles();

                for (int i = 0; i < fileList.size(); i++) {

                    file = (InstallFile) fileList.elementAt(i);

                    index = file.localFile.lastIndexOf(File.separatorChar);

                    fileName = file.localFile.substring(index);

                    copyFile(file.localFile, dirName + fs("/webinstall") + fileName);

                }

            } catch (IOException ioe) {

                try {

                    MessageBox mb = new MessageBox();

                    mb.label.setText("There was an error while deploying the project: " + ioe);

                    mb.show();

                } catch (Exception e) {

                }

            }

        }

    }
