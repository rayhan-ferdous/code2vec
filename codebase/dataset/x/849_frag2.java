    public Session(AbstractChannelService service, SocketChannel channel, ProtocolFilterChain fc, MessageHandlerChain hc) {

        this.service = service;

        this.channel = channel;

        this.filterChain = fc;

        this.handlerChain = hc;

    }
