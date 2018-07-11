            if (doPurgeSeries(w, rec, counter)) {

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



    private boolean doPurgeSeries(DirWriter w, DirRecord parent, int[] counter) throws IOException {
