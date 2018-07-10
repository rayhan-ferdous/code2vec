    public String[] listFiles(String path) {

        ObjectArray<String> list = ObjectArray.newInstance();

        synchronized (MEMORY_FILES) {

            for (String name : MEMORY_FILES.keySet()) {

                if (name.startsWith(path)) {

                    list.add(name);

                }

            }

            String[] array = new String[list.size()];

            list.toArray(array);

            return array;

        }

    }
