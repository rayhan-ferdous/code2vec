    public void destroy() {

        try {

            cache.destroy();

        } catch (Exception e) {

            log.warn("could not destroy cache", e);

        }

    }
