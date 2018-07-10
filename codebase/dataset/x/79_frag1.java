        bind(BlueprintsTabFirstLoadActionRunner.class).to(BlueprintsTabFirstLoadActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintAddActionRunner.class).to(BlueprintAddActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintSaveActionRunner.class).to(BlueprintSaveActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintDeleteActionRunner.class).to(BlueprintDeleteActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintGetDetailsActionRunner.class).to(BlueprintGetDetailsActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintsImportActionRunner.class).to(BlueprintsImportActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintsReloadActionRunner.class).to(BlueprintsReloadActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintsReloadForCorporationActionRunner.class).to(BlueprintsReloadForCorporationActionRunnerImpl.class).in(Singleton.class);

        bind(BlueprintsReloadForAllianceActionRunner.class).to(BlueprintsReloadForAllianceActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetTabFirstLoadActionRunner.class).to(PriceSetTabFirstLoadActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetAddItemActionRunner.class).to(PriceSetAddItemActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetCopyActionRunner.class).to(PriceSetCopyActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetCreateActionRunner.class).to(PriceSetCreateActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetDeleteActionRunner.class).to(PriceSetDeleteActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadActionRunner.class).to(PriceSetLoadActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadNamesActionRunner.class).to(PriceSetLoadNamesActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetRenameActionRunner.class).to(PriceSetRenameActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetSaveActionRunner.class).to(PriceSetSaveActionRunnerImpl.class).in(Singleton.class);

        bind(PriceFetchFromEveCentralActionRunner.class).to(PriceFetchFromEveCentralActionRunnerImpl.class).in(Singleton.class);

        bind(PriceFetchFromEveMetricsActionRunner.class).to(PriceFetchFromEveMetricsActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadForCorporationActionRunner.class).to(PriceSetLoadForCorporationActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadForAllianceActionRunner.class).to(PriceSetLoadForAllianceActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadCorporationNamesActionRunner.class).to(PriceSetLoadCorporationNamesActionRunnerImpl.class).in(Singleton.class);

        bind(PriceSetLoadAllianceNamesActionRunner.class).to(PriceSetLoadAllianceNamesActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesTabFirstLoadActionRunner.class).to(PreferencesTabFirstLoadActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesAddCharacterActionRunner.class).to(PreferencesAddCharacterActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesDeleteCharacterActionRunner.class).to(PreferencesDeleteCharacterActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesSetMainCharacterActionRunner.class).to(PreferencesSetMainCharacterActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesAddApiKeyActionRunner.class).to(PreferencesAddApiKeyActionRunnerImpl.class).in(Singleton.class);

        bind(PreferencesDeleteApiKeyActionRunner.class).to(PreferencesDeleteApiKeyActionRunnerImpl.class).in(Singleton.class);
