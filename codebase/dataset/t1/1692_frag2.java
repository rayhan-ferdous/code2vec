    public void getBytes(int index, ByteBuffer dst) {

        int sliceId = sliceId(index);

        int limit = dst.limit();

        int length = dst.remaining();

        if (index > capacity() - length) {

            throw new IndexOutOfBoundsException();

        }

        int i = sliceId;

        try {

            while (length > 0) {

                ChannelBuffer s = slices[i];

                int adjustment = indices[i];

                int localLength = Math.min(length, s.capacity() - (index - adjustment));

                dst.limit(dst.position() + localLength);

                s.getBytes(index - adjustment, dst);

                index += localLength;

                length -= localLength;

                i++;

            }

        } finally {

            dst.limit(limit);

        }

    }
