    public Object clone() {

        try {

            @SuppressWarnings("unchecked") ArrayDelegatingEList<E> clone = (ArrayDelegatingEList<E>) super.clone();

            Object[] data = data();

            int size = data == null ? 0 : data.length;

            if (size > 0) {

                Object[] newData = newData(size);

                clone.setData(newData);

                System.arraycopy(data, 0, newData, 0, size);

            }

            return clone;

        } catch (CloneNotSupportedException exception) {

            throw new InternalError();

        }

    }
