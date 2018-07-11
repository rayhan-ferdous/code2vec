    private void addDirBuilderPrefElem(HashMap map, String key) {

        if (!key.startsWith("dir.")) {

            return;

        }

        int pos2 = key.lastIndexOf('.');

        String type = key.substring(4, pos2).replace('_', ' ');

        Dataset ds = (Dataset) map.get(type);

        if (ds == null) {

            map.put(type, ds = dof.newDataset());

        }

        int tag = Tags.forName(key.substring(pos2 + 1));

        ds.putXX(tag, VRMap.DEFAULT.lookup(tag));

    }
