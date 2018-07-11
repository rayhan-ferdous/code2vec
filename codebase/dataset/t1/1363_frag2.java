    private void initOverwrite(Configuration cfg) {

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("set.")) {

                try {

                    overwrite.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal entry in dcmsnd.cfg - " + key + "=" + cfg.getProperty(key));

                }

            }

        }

    }
