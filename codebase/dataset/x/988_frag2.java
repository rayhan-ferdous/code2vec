    public Class[] getClasses() throws IOException {

        Vector<Class> classes = new Vector<Class>();

        for (int i = 0; i < jarFiles.length; i++) {

            try {

                JarFile file = new JarFile(FileUtils.convertFromJarURL(jarFiles[i]).getFile());

                for (Enumeration<JarEntry> e = file.entries(); e.hasMoreElements(); ) {

                    String classname = e.nextElement().toString();

                    if (classname.endsWith(".class")) {

                        classname = classname.substring(0, classname.indexOf("."));

                        classname = classname.replaceAll("/", ".");

                        Class typeClass = loadClass(classname);

                        classes.add(typeClass);

                    }

                }

            } catch (ClassNotFoundException e) {

                throw new RemoteException("Class not found" + e.getMessage());

            }

        }

        return classes.toArray(new Class[0]);

    }
