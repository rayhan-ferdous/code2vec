    private void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {

        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

        while (src.read(buffer) != -1) {

            buffer.flip();

            dest.write(buffer);

            buffer.compact();

        }

        buffer.flip();

        while (buffer.hasRemaining()) {

            dest.write(buffer);

        }

    }
