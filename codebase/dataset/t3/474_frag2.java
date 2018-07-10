    protected Entry nearestEntry(Entry newEntry) {

        assert (!this.entries[0].isEmpty());

        Entry res = entries[0];

        double min = res.calcDistance(newEntry);

        for (int i = 1; i < entries.length; i++) {

            if (this.entries[i].isEmpty()) {

                break;

            }

            Entry entry = entries[i];

            double distance = entry.calcDistance(newEntry);

            if (distance < min) {

                min = distance;

                res = entry;

            }

        }

        return res;

    }
