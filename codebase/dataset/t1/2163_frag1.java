    public void init(ForceSimulator fsim) {

        clear();

        float x1 = Float.MAX_VALUE, y1 = Float.MAX_VALUE;

        float x2 = Float.MIN_VALUE, y2 = Float.MIN_VALUE;

        Iterator itemIter = fsim.getItems();

        while (itemIter.hasNext()) {

            ForceItem item = (ForceItem) itemIter.next();

            float x = item.location[0];

            float y = item.location[1];

            if (x < x1) x1 = x;

            if (y < y1) y1 = y;

            if (x > x2) x2 = x;

            if (y > y2) y2 = y;

        }

        float dx = x2 - x1, dy = y2 - y1;

        if (dx > dy) {

            y2 = y1 + dx;

        } else {

            x2 = x1 + dy;

        }

        setBounds(x1, y1, x2, y2);

        itemIter = fsim.getItems();

        while (itemIter.hasNext()) {

            ForceItem item = (ForceItem) itemIter.next();

            insert(item);

        }

        calcMass(root);

    }
