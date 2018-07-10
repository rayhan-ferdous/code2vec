    public static List getNodes(List bpmAgents) {

        int count = bpmAgents.size();

        List bpmNodes = new ArrayList(count);

        for (int index = 0; index < count; index++) {

            BpmAgent bpmAgent = (BpmAgent) bpmAgents.get(index);

            bpmNodes.add(bpmAgent.getNode());

        }

        return bpmNodes;

    }
