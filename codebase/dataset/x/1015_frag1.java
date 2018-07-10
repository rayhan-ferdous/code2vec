    public void initializeBackupManager() {

        OpGraph graph = new OpGraph();

        for (OpPrototype prototype : OpTypeManager.getPrototypes()) {

            if (prototype.isInterface()) {

                continue;

            }

            graph.addNode(prototype);

            OpGraph.Entry prototypeNode = graph.getNode(prototype);

            List dependencies = prototype.getSubsequentBackupDependencies();

            Iterator iter = dependencies.iterator();

            while (iter.hasNext()) {

                OpPrototype dependency = (OpPrototype) iter.next();

                OpGraph.Entry dependencyNode = graph.getNode(dependency);

                if (dependencyNode == null) {

                    dependencyNode = graph.addNode(dependency);

                }

                if (((OpPrototype) dependencyNode.getElem()).getInstanceClass() == OpSiteObject.class) {

                    graph.removeEdge(prototypeNode, dependencyNode);

                }

                graph.addEdge(dependencyNode, prototypeNode, DEFAULT_EDGE_CLASS);

            }

        }

        Iterator iter = graph.getTopologicOrder().iterator();

        while (iter.hasNext()) {

            OpGraph.Entry node = (OpGraph.Entry) iter.next();

            if (!((OpPrototype) node.getElem()).subTypes().hasNext()) {

                addPrototype((OpPrototype) node.getElem());

            }

        }

    }
