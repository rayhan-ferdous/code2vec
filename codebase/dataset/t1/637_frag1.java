        long time = System.currentTimeMillis();

        int index = 0;

        if (list.size() == 0) {

            timeCounter += System.currentTimeMillis() - time;

            return null;

        } else {

            index = getNearestIndex(orderingValue);

        }

        long order = listOrdering.get(index);

        if (order >= orderingValue) {
