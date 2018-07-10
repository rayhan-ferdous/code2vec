    public static IFolder getParentFolder(IResource resource) {

        if (resource == null) {

            return null;

        }

        IContainer parent = resource.getParent();

        if (parent == null || parent.getType() != IResource.FOLDER) {

            return null;

        }

        return (IFolder) parent;

    }
