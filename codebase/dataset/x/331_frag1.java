    @Override

    public boolean equals(Object ob) {

        if (!(ob instanceof Counter)) {

            return false;

        }

        return 0 == compareTo((Counter) ob);

    }
