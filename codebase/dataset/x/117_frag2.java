    public static List<File> filterFiles(Collection<File> jars) {

        List<File> filtered = new ArrayList<File>();

        Map<String, List<File>> filemap = new HashMap<String, List<File>>();

        for (File lf : jars) {

            String[] names = FileUtils.getJarBaseNameAndSuffix(lf.getName());

            String name = names[0];

            List<File> grp = filemap.get(name);

            if (grp == null) {

                grp = new ArrayList<File>();

                filemap.put(name, grp);

            }

            grp.add(lf);

        }

        for (List<File> fl : filemap.values()) {

            if (!fl.isEmpty()) {

                Collections.sort(fl, new Comparator<File>() {



                    @Override

                    public int compare(File o1, File o2) {

                        return o2.getName().compareTo(o1.getName());

                    }

                });

                filtered.add(fl.get(0));

            }

        }

        return filtered;

    }
