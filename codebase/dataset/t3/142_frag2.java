    public int transferHeaderAndDataFrom(PcapHeader header, JBuffer buffer) {

        JBuffer b = getMemoryBuffer(header.size() + buffer.size());

        int o = header.transferTo(b, 0);

        o += buffer.transferTo(b, 0, buffer.size(), o);

        peerHeaderAndData(b);

        return o;

    }
