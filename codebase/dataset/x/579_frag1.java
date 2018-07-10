    private void loadModelPlugins(Scenario scenario) throws PluginDefinitionException, PluginLifecycleException {

        modelPluginLoader.removePlugins();

        File modelPluginPath = scenario.getModelPluginPath();

        if (modelPluginPath != null) {

            modelPluginLoader.addPath(modelPluginPath);

            modelPluginLoader.publishPlugins();

        }

        UIExtPointLoader loader = new UIExtPointLoader(gui);

        loader.processExtPoints(modelPluginLoader.getManager());

        if (!loader.isTickFormatterLoaded()) {

            gui.setTickCountFormatter(new DefaultTickCountFormatter());

        }

    }
