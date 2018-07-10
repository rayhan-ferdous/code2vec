        public static void deleteFile(File file) {

            if (file.exists()) {

                if (!file.isDirectory()) {

                    file.delete();

                } else {

                    File[] children = file.listFiles();

                    if (children != null && children.length > 0) {

                        for (File child : children) {

                            deleteFile(child);

                        }

                    }

                    file.delete();

                }

            }

        }
