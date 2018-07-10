    public final void write(final double[] src, int offs, final int len) throws IOException {

        CheckBounds.offset(src.length, offs, len);

        final int toIndex = offs + len;

        while (offs < toIndex) {

            final ByteBuffer buf = ensureOpen(Math.max(8, (toIndex - offs) << 3));

            final int step = Math.min(toIndex - offs, buf.remaining() >> 3);

            final int pos = buf.position();

            for (int i = 0; i < step; i++) buf.putDouble(pos + (i << 3), src[offs + i]);

            buf.position(pos + (step << 3));

            offs += step;

        }

    }
