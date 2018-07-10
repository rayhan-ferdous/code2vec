    private XStream getXStream() {

        XStream xs = new XStream();

        xs.alias("session", SessionImpl.class);

        xs.alias("map", JDBMMap.class);

        if (ms != null) {

            ms.registerAliases(xs);

        }

        xs.setMode(xs.NO_REFERENCES);

        return xs;

    }
