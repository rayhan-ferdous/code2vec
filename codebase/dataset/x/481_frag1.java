    public void buildOnlineModel() {

        try {

            model.setSynchronizationMode(Scenario.SYNC_MODE_LIVE);

            model.resync();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }
