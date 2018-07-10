    private static DefaultTreeModel loadDirectoryContent(File directory, DefaultMutableTreeNode parent, DefaultTreeModel model) {

        if (directory != null) {

            DefaultMutableTreeNode currentRoot = new DefaultMutableTreeNode(directory);

            model.insertNodeInto(currentRoot, parent, parent.getChildCount());

            File[] content = directory.listFiles();

            if (content != null) {

                for (int i = 0; i < content.length; i++) {

                    if (content[i].isDirectory()) {

                        loadDirectoryContent(content[i], currentRoot, model);

                    } else {

                        model.insertNodeInto(new DefaultMutableTreeNode(content[i]), currentRoot, currentRoot.getChildCount());

                    }

                }

            }

        }

        return model;

    }
