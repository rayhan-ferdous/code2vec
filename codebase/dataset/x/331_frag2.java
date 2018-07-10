    @Override

    public boolean equals(Object o) {

        if (!(o instanceof Test)) {

            return false;

        }

        return Arrays.equals(classes, ((Test) o).classes);

    }
