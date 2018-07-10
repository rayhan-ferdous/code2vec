    @Override

    public double[] getClasses() {

        if (classes != null) return classes;

        List<Double> tempClasses = new ArrayList<Double>();

        try {

            for (List<Object> record : records) {

                if (!tempClasses.contains(record.get(targetAttribute))) tempClasses.add(CommonUtils.objectToDouble(record.get(targetAttribute)));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        classes = new double[tempClasses.size()];

        for (int i = 0; i < tempClasses.size(); i++) classes[i] = tempClasses.get(i);

        return classes;

    }
