    public double getLat(String str) {

        String n = stations.get(str);

        String s = "-9999";

        double d;

        try {

            s = n.substring(37, 42);

            d = Double.valueOf(s) / 100;

        } catch (Exception e) {

            d = -9999;

        }

        if (s.equals("-9999")) d = -99999;

        return d;

    }
