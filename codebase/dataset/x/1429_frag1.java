    public boolean removeAll(Collection c) {

        Iterator it = c.iterator();

        boolean retVal = false;

        while (it.hasNext()) {

            Object element = it.next();

            if (remove(element)) {

                retVal = true;

            }

        }

        return retVal;

    }
