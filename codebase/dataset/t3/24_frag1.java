    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {

        File dir = new File(packagePath);

        if (!dir.exists() || !dir.isDirectory()) {

            log.warn("用户定义包名 " + packageName + " 下没有任何文件");

            return;

        }

        File[] dirfiles = dir.listFiles(new FileFilter() {



            public boolean accept(File file) {

                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));

            }

        });

        for (File file : dirfiles) {

            if (file.isDirectory()) {

                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);

            } else {

                String className = file.getName().substring(0, file.getName().length() - 6);

                try {

                    classes.add(Class.forName(packageName + '.' + className));

                } catch (ClassNotFoundException e) {

                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");

                    e.printStackTrace();

                }

            }

        }

    }
