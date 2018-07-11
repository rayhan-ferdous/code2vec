    public ByteBuffer toByteBuffer(int index, int length) {

        if (slices.length == 1) {

            return slices[0].toByteBuffer(index, length);

        }

        ByteBuffer[] buffers = toByteBuffers(index, length);

        ByteBuffer merged = ByteBuffer.allocate(length).order(order());

        for (ByteBuffer b : buffers) {

            merged.put(b);

        }

        merged.flip();

        return merged;

    }
