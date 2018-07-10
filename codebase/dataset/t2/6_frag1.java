    @Override

    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

        ChannelBuffer msg = (ChannelBuffer) e.getMessage();

        synchronized (trafficLock) {

            outboundChannel.write(msg);

            if (!outboundChannel.isWritable()) {

                e.getChannel().setReadable(false);

            }

        }

    }
