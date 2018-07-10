    protected long loadFromSource(long afterThisTime) {

        setFromTime(afterThisTime + 1);

        long loadedTime = getLoadedTime();

        if (!connect()) {

            return loadedTime;

        }

        try {

            request();

            loadedTime = read();

        } catch (Exception ex) {

            System.out.println("Error in loading from source: " + ex.getMessage());

        }

        return loadedTime;

    }
