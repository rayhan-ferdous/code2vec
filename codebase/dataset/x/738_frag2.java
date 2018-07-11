            FastDB.printEntity("IgnoreList");

            FastDB.removeEntity("IgnoreList", key);

            FastDB.printEntity("IgnoreList");

            Vector<Object> IgnoreListV = null;

            try {

                IgnoreListV = FastDB.queryEntityListByIndex("IgnoreList", "spriteid", new Long(a));

            } catch (Exception e) {

                e.printStackTrace();
