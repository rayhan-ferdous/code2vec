    public String[] benchmarks() {

        String[] l = new String[benchmarks.length];

        for (int i = 0; i < benchmarks.length; i++) {

            l[i] = getName(benchmarks[i]);

        }

        return l;

    }
