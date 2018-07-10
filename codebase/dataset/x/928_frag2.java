    public IRepositoryOptions getRepositoryOptions() {

        if (repositoryOptions == null) {

            repositoryOptions = createRepositoryOptions();

            if (optionListener == null) {

                optionListener = new OptionListener();

            }

            repositoryOptions.addXfsrOptionListener(optionListener);

        }

        return repositoryOptions;

    }
