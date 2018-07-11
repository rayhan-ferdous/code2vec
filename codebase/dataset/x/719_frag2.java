        Protocol protocol = negotiateProtocol();

        replicaJEVersions = (ReplicaJEVersions) protocol.read(namedChannel);

        LoggerUtils.fine(logger, repNode.getRepImpl(), " Replica " + replicaNameIdPair.getName() + " JE version: " + replicaJEVersions.getVersion().getVersionString() + " Log version: " + replicaJEVersions.getLogVersion());

        JEVersionsReject reject = checkJECompatibility(protocol, replicaJEVersions);

        if (reject != null) {
