    public void afterPacks(AutomatedInstallData idata, AbstractUIProgressHandler handler) throws Exception {

        if (informProgressBar()) {

            handler.nextStep(getMsg("BSFAction.pack"), getProgressBarCallerId(), getActionCount(idata));

        }

        for (Object selectedPack : idata.selectedPacks) {

            String currentPack = ((Pack) selectedPack).name;

            performAllActions(currentPack, ActionBase.AFTERPACKS, handler, new Object[] { idata, handler });

        }

        if (uninstActions.size() > 0) {

            UninstallData.getInstance().addAdditionalData("bsfActions", uninstActions);

        }

        installdata = null;

    }
