        return false;

    }



    private boolean doPurgeSeries(DirWriter w, DirRecord parent, int[] counter) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (doPurgeInstances(w, rec, counter)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[0] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean doPurgeInstances(DirWriter w, DirRecord parent, int[] counter) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {
