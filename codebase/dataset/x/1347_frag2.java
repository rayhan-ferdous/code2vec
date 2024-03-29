    private synchronized void writeChunk(ChannelBuffer buf, Channel ch) {

        SequentialChunkEvent ev = new SequentialChunkEvent();

        ev.setHash(getID());

        ev.sequence = writeSequence.getAndIncrement();

        ev.content = new byte[buf.readableBytes()];

        buf.readBytes(ev.content);

        if (ev.content.length > 512) {

            CompressEventV2 wrap = new CompressEventV2(CompressorType.QUICKLZ, ev);

            wrap.setHash(ev.getHash());

            EventService.getInstance(sessionManager.getUserToken()).offer(wrap, ch);

        } else {

            EventService.getInstance(sessionManager.getUserToken()).offer(ev, ch);

        }

    }
