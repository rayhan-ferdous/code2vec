    public final void write(final boolean[] src, int offs, final int len) throws IOException {

        CheckBounds.offset(src.length, offs, len);

        if (len > 0) {

            final int toIndex = offs + len;

            do {

                final ByteBuffer buf = ensureOpen(toIndex - offs);

                final int step = Math.min(toIndex - offs, buf.remaining());

                final int pos = buf.position();

                for (int i = 0; i < step; i++) buf.put(pos + i, src[offs + i] ? (byte) 1 : 0);

                buf.position(pos + step);

                offs += step;

            } while (offs < toIndex);

        }

    }
