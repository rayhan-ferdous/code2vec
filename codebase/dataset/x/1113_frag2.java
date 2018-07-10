    public static FileFilter getExtensionsFilter(final String[] extensions) {

        return new FileFilter() {



            public boolean accept(File pathname) {

                for (int i = 0; i < extensions.length; i++) {

                    if (pathname.isDirectory()) {

                        return false;

                    }

                    String extension = extensions[i];

                    if (pathname.getName().endsWith(extension)) {

                        return true;

                    }

                }

                return false;

            }

        };

    }
