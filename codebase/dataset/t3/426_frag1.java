    public TestZFailureExceptionCM(String name) {

        super(name);

        ChannelServiceFactory basic = ChannelServiceFactory.createFactory(null);

        Map<String, Object> props = new HashMap<String, Object>();

        props.put(ChannelServiceFactory.KEY_IMPLEMENTATION_CLASS, ChannelServiceFactory.VAL_PACKET_CHANNEL_MGR);

        props.put(ChannelServiceFactory.KEY_CHILD_CHANNELMGR_FACTORY, basic);

        ChannelServiceFactory packetFactory = ChannelServiceFactory.createFactory(props);

        Map<String, Object> props2 = new HashMap<String, Object>();

        props2.put(ChannelServiceFactory.KEY_IMPLEMENTATION_CLASS, ChannelServiceFactory.VAL_EXCEPTION_CHANNEL_MGR);

        props2.put(ChannelServiceFactory.KEY_CHILD_CHANNELMGR_FACTORY, packetFactory);

        factory = ChannelServiceFactory.createFactory(props2);

        FactoryCreator creator = FactoryCreator.createFactory(null);

        procFactory = creator.createPacketProcFactory(null);

        factoryHolder = new Settings(null, procFactory);

    }
