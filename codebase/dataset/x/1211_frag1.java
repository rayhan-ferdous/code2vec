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

        bind(QuickCalculatorTabFirstLoadActionRunner.class).to(QuickCalculatorTabFirstLoadActionRunnerImpl.class).in(Singleton.class);

        bind(QuickCalculatorSetActionRunner.class).to(QuickCalculatorSetActionRunnerImpl.class).in(Singleton.class);

        bind(QuickCalculatorDirectSetActionRunner.class).to(QuickCalculatorDirectSetActionRunnerImpl.class).in(Singleton.class);

        bind(QuickCalculatorUseBlueprintActionRunner.class).to(QuickCalculatorUseBlueprintActionRunnerImpl.class).in(Singleton.class);

        bind(QuickCalculatorUseAllBlueprintsActionRunner.class).to(QuickCalculatorUseAllBlueprintsActionRunnerImpl.class).in(Singleton.class);
