    @Test(expectedExceptions = BufferUnderflowException.class)

    public void decodeWithBufferUnderflow() {

        EbmlDecoder decoder = new EbmlDecoder();

        ByteBuffer buffer = ByteBuffer.allocate(32);

        buffer.limit(7);

        int position = buffer.position();

        try {

            decoder.decodeFloatingPoint(buffer, 8);

        } finally {

            assertThat(buffer.position(), isEqualTo(position));

        }

    }
