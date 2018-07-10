        if (aSelection == null || aSelection.isEmpty()) {

            return;

        }

        if (false == aSelection.getFirstElement() instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiAbstractNavigatorItem) {

            return;

        }

        SensorDataWebGui.diagram.navigator.SensorDataWebGuiAbstractNavigatorItem abstractNavigatorItem = (SensorDataWebGui.diagram.navigator.SensorDataWebGuiAbstractNavigatorItem) aSelection.getFirstElement();

        View navigatorView = null;

        if (abstractNavigatorItem instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) {

            navigatorView = ((SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) abstractNavigatorItem).getView();

        } else if (abstractNavigatorItem instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup) {
