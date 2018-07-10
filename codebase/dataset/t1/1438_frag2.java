    public Zone findBestZone(Name name) {

        Zone foundzone = null;

        foundzone = (Zone) znames.get(name);

        if (foundzone != null) return foundzone;

        int labels = name.labels();

        for (int i = 1; i < labels; i++) {

            Name tname = new Name(name, i);

            foundzone = (Zone) znames.get(tname);

            if (foundzone != null) return foundzone;

        }

        return null;

    }
