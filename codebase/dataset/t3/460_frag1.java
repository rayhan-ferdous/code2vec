    public void onDestroy() {

        loadNotifierTimerTask.cancel();

        registry.deregisterStoreServiceAddress(serviceAddress);

        CacheManager.getInstance().removeCache(ctx.getLocaleAddress().getAddress() + ":" + ctx.getLocalePort());

        cache.dispose();

        try {

            groupEndpoint.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
