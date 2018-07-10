            } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {

                throw new IndexOutOfBoundsException();

            } else if (len == 0) {
