    public Entry nearestEntry(ClusKernel buffer) {

        Entry res = entries[0];

        double min = res.calcDistance(buffer);

        for (int i = 1; i < entries.length; i++) {

            Entry entry = entries[i];

            if (entry.isEmpty()) {

                break;

            }

            double distance = entry.calcDistance(buffer);

            if (distance < min) {

                min = distance;

                res = entry;

            }

        }

        return res;

    }
