    private void setStrikethroughMetrics(ByteBuffer os_2Table, int upem) {

        if (os_2Table == null || os_2Table.capacity() < 30 || upem < 0) {

            stSize = .05f;

            stPos = -.4f;

            return;

        }

        ShortBuffer sb = os_2Table.asShortBuffer();

        stSize = sb.get(13) / (float) upem;

        stPos = -sb.get(14) / (float) upem;

    }
