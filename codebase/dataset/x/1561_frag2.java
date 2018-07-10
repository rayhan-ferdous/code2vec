    public static void copyFile(File file, IFolder destination, IProgressMonitor monitor) {

        String name = file.getName();

        IFile newFile = destination.getFile(name);

        FileInputStream input = null;

        try {

            input = new FileInputStream(file);

            if (newFile.exists()) {

                newFile.setContents(input, true, false, monitor);

            } else {

                newFile.create(input, true, monitor);

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (input != null) {

                try {

                    input.close();

                } catch (IOException e) {

                    NanoVMUI.log(e);

                }

            }

        }

        monitor.worked(1);

    }
