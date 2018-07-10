    public static synchronized void saveConfiguration() {

        final Set<String> groups = propertiesByGroup.keySet();

        final Properties amalgamatedProperties = new Properties();

        for (final String groupName : groups) {

            final Properties groupProperties = propertiesByGroup.get(groupName);

            final Set<Object> keys = groupProperties.keySet();

            for (final Object key : keys) {

                final StringBuffer keyBuffer = new StringBuffer(groupName).append('.');

                keyBuffer.append(key.toString());

                amalgamatedProperties.setProperty(keyBuffer.toString(), groupProperties.getProperty(key.toString()));

            }

        }

        OutputStream out = null;

        try {

            out = new FileOutputStream(configFileName);

            amalgamatedProperties.store(out, null);

        } catch (final Exception e) {

            log.warn("An error occurred while saving the configuration.", e);

            throw new RuntimeException(e);

        } finally {

            IOUtils.closeQuietly(out);

        }

    }
