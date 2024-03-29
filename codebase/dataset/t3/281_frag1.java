    public Object clone() {

        try {

            @SuppressWarnings("unchecked") BasicEList<E> clone = (BasicEList<E>) super.clone();

            if (size > 0) {

                clone.size = size;

                clone.data = newData(size);

                System.arraycopy(data, 0, clone.data, 0, size);

            }

            return clone;

        } catch (CloneNotSupportedException exception) {

            throw new InternalError();

        }

    }
