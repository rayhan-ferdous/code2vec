    public void start() {

        fButtonRegatta.addActionListener(this);

        fButtonEntries.addActionListener(this);

        fButtonRaces.addActionListener(this);

        fItemNew.addActionListener(this);

        fItemSave.addActionListener(this);

        fItemSaveAs.addActionListener(this);

        fItemOpen.addActionListener(this);

        fItemRestore.addActionListener(this);

        fItemHelp.addActionListener(this);

        fItemExit.addActionListener(this);

        fItemRecent1.addActionListener(this);

        fItemRecent2.addActionListener(this);

        fItemRecent3.addActionListener(this);

        fItemShowReports.addActionListener(this);

        fItemReportOptions.addActionListener(this);

        fItemPreferences.addActionListener(this);

        if (sDialogEntries != null && sDialogEntries.isVisible()) sDialogEntries.start();

        if (sDialogRaces != null && sDialogRaces.isVisible()) sDialogRaces.start();

        if (sDialogRegatta != null && sDialogRegatta.isVisible()) sDialogRegatta.startUp();

        if (sDialogPreferences != null && sDialogPreferences.isVisible()) sDialogPreferences.startUp();

    }
