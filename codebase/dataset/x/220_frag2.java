    private void addEndSlashIfNecessary(final URL url) {

        final String urlAsString = url.toExternalForm();

        if (urlAsString.toLowerCase().startsWith("http")) {

            if (urlAsString.endsWith(url.getHost())) {

                try {

                    this.fetchedResource.setURL(new URL(urlAsString + "/"));

                } catch (final MalformedURLException e) {

                }

            }

        }

    }
