            int out_len = in_len * 8;

            if (bytebuffer == null || bytebuffer.capacity() < out_len) {

                bytebuffer = ByteBuffer.allocate(out_len).order(ByteOrder.BIG_ENDIAN);

                floatbuffer = bytebuffer.asDoubleBuffer();

            }

            floatbuffer.position(0);
