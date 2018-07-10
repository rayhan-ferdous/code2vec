    public Long importGraph(Long graphId, String xml) {

        ONDEXGraph graph = ONDEXGraphRegistry.graphs.get(graphId);

        try {

            read(graph, new StringReader(xml));

            databases.get(graph.getSID()).commit();

        } catch (IOException e) {

            logger.error("Error importing into graph '" + graph.getName() + "'", e);

        } catch (XMLStreamException e) {

            logger.error("Error importing into graph '" + graph.getName() + "'", e);

        } catch (ClassNotFoundException e) {

            logger.error("Error importing into graph '" + graph.getName() + "'", e);

        }

        return new WSGraph(graph).getId();

    }
