    private boolean matchFileIDs(String[] ids) {

        if (ids == null || fileIDs.isEmpty()) {

            return false;

        }

        for (Iterator iter = fileIDs.iterator(); iter.hasNext(); ) {

            if (Arrays.equals((String[]) iter.next(), ids)) {

                return true;

            }

        }

        return false;

    }
