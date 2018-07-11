    public WkaBasedMembershipScheme(ManagedChannel channel, OperationMode mode, List<MembershipManager> applicationDomainMembershipManagers, MembershipManager primaryMembershipManager, Map<String, Parameter> parameters, byte[] domain, List<Member> members, boolean atmostOnceMessageSemantics, boolean preserverMsgOrder) {

        this.channel = channel;

        this.mode = mode;

        this.applicationDomainMembershipManagers = applicationDomainMembershipManagers;

        this.primaryMembershipManager = primaryMembershipManager;

        this.parameters = parameters;

        this.localDomain = domain;

        this.members = members;

        this.atmostOnceMessageSemantics = atmostOnceMessageSemantics;

        this.preserverMsgOrder = preserverMsgOrder;

    }
