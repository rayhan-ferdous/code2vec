    public void start(BundleContext context) throws Exception {

        super.start(context);

        plugin = this;

        this.injector = Guice.createInjector(new RadioModule(), new DownloaderRuntimeModule());

        File baseDir = new File(Activator.baseDir());

        if (!baseDir.exists()) if (!baseDir.mkdirs()) {

            String message = "cannot create directory " + baseDir();

            MessageDialog.openError(null, "creating directory", message);

            throw new IllegalStateException(message);

        }

        File[] listFolders = baseDir.listFiles();

        for (File oneFolder : listFolders) {

            if (oneFolder.isDirectory()) {

                IOUtils.deleteDirectory(oneFolder);

            }

        }

        getRadioScheduler().addSchedulerListener(this.shutdownSystem);

    }
