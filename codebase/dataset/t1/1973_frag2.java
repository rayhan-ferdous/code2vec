    private int doPurge(DirWriter w) throws IOException {

        int[] counter = { 0 };

        for (DirRecord rec = w.getFirstRecord(true); rec != null; rec = rec.getNextSibling(true)) {

            if (doPurgeStudy(w, rec, counter)) {

                counter[0] += w.remove(rec);

            }

        }

        return counter[0];

    }
