    public void deploy(DeploymentFileData deploymentFileData) throws DeploymentException {

        boolean isDirectory = deploymentFileData.getFile().isDirectory();

        try {

            deploymentFileData.setClassLoader(isDirectory, this.axisConfiguration.getServiceClassLoader(), (File) this.axisConfiguration.getParameterValue(Constants.Configuration.ARTIFACTS_TEMP_DIR));

            ClassLoader deploymentClassLoader = deploymentFileData.getClassLoader();

            String absolutePath = deploymentFileData.getFile().getAbsolutePath();

            Config configObject = getConfig(absolutePath);

            Configurator configurator = getConfigurator(configObject, deploymentClassLoader);

            Service[] services = configObject.getServices().getService();

            ClassDeployer classDeployer = new ClassDeployer(configurationContext, deploymentClassLoader, configurator);

            Class serviceClass;

            for (int i = 0; i < services.length; i++) {

                serviceClass = Loader.loadClass(deploymentClassLoader, services[i].getServiceClass());

                classDeployer.deployClass(serviceClass);

            }

            log.info("Deployed RMI Services with deployment file " + deploymentFileData.getName());

        } catch (ClassNotFoundException e) {

            throw new DeploymentException("Service class not found", e);

        } catch (AxisFault axisFault) {

            throw new DeploymentException("axis fault", axisFault);

        } catch (IOException e) {

            throw new DeploymentException("zip file not found", e);

        } catch (ConfigFileReadingException e) {

            throw new DeploymentException("config file reading problem", e);

        }

    }
