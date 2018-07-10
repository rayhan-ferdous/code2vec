    public void cleanRepository() {

        try {

            FileUtils.cleanDirectory(new File(getBaseUrl()));

        } catch (IOException e) {

            logger.error(String.format("Error when clean repository [%s]", getBaseUrl()));

        }

    }
