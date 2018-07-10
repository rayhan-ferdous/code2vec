    private boolean doRemoveStudy(DirWriter w, DirRecord parent, int[] counter, boolean delFiles) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (studyUIDs.contains(rec.getDataset().getString(Tags.StudyInstanceUID))) {

                if (delFiles) {

                    deleteRefFiles(w, rec, counter);

                }

                toRemove.add(rec);

            } else if (doRemoveSeries(w, rec, counter, delFiles)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[1] += w.remove((DirRecord) it.next());

        }

        return false;

    }
