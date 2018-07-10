    public void addRepository(String repository) {

        if (repository.startsWith("/WEB-INF/lib") || repository.startsWith("/WEB-INF/classes")) return;

        try {

            URL url = new URL(repository);

            super.addURL(url);

            hasExternalRepositories = true;

            repositoryURLs = null;

        } catch (MalformedURLException e) {

            IllegalArgumentException iae = new IllegalArgumentException("Invalid repository: " + repository);

            iae.initCause(e);

            throw iae;

        }

    }
