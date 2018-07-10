                public boolean accept(java.io.File dir, String name) {

                    File file = new java.io.File(dir, name);

                    if (file.isFile()) {

                        list.add(file);

                        return true;

                    } else {

                        file.listFiles(this);

                    }

                    return false;

                }
