    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        if (e instanceof ChannelStateEvent) {

            logger.info(e.toString());

        }

        super.handleUpstream(ctx, e);

    }
