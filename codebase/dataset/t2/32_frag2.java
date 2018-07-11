    private final void initPresContext(int pcid, List val) {

        Iterator it = val.iterator();

        String as = UIDs.forName((String) it.next());

        String[] tsUIDs = new String[val.size() - 1];

        for (int i = 0; i < tsUIDs.length; ++i) {

            tsUIDs[i] = UIDs.forName((String) it.next());

        }

        assocRQ.addPresContext(aFact.newPresContext(pcid, as, tsUIDs));

    }



    private void initOverwrite(Configuration config) {
