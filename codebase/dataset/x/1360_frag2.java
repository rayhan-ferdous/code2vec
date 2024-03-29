    public List<DirectoryFolderNode> listFoldersNodes(DirectoryFolder parentFolder, String sortingProperty, String sortingOrder) {

        try {

            if (log.isDebugEnabled()) log.debug("Listing folders of :" + parentFolder.getURLPath());

            Node parentNode = JCRUtil.getNodeById(parentFolder.getId(), parentFolder.getWorkspace());

            NodeIterator nodeChildren = parentNode.getNodes(JCRUtil.FOLDER_PREFIX);

            List<DirectoryFolderNode> directoryFolderNodes = new ArrayList<DirectoryFolderNode>();

            while (nodeChildren.hasNext()) {

                Node itemNode = (Node) nodeChildren.next();

                directoryFolderNodes.add(new DirectoryFolderNode(itemNode));

            }

            return directoryFolderNodes;

        } catch (RepositoryException e) {

            String errorMessage = "Error accessing the repository: ";

            log.error(errorMessage, e);

            throw new CMSRuntimeException(errorMessage, e);

        }

    }
