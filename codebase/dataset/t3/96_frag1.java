    public Object[] toArray(Object[] a) {

        boolean wasInterrupted = beforeRead();

        try {

            return c_.toArray(a);

        } finally {

            afterRead(wasInterrupted);

        }

    }
