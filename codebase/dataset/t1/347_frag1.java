        if (aSelection == null || aSelection.isEmpty()) {

            return;

        }

        if (false == aSelection.getFirstElement() instanceof FilesystemAbstractNavigatorItem) {

            return;

        }

        FilesystemAbstractNavigatorItem abstractNavigatorItem = (FilesystemAbstractNavigatorItem) aSelection.getFirstElement();

        View navigatorView = null;

        if (abstractNavigatorItem instanceof FilesystemNavigatorItem) {

            navigatorView = ((FilesystemNavigatorItem) abstractNavigatorItem).getView();

        } else if (abstractNavigatorItem instanceof FilesystemNavigatorGroup) {
