        if (system.hasAccess("Controle de Cheques")) {

            MenuItem checkControlCenter = new MenuItem(financialMenu, SWT.NONE);

            checkControlCenter.setText("Controle de Cheques");

            checkControlCenter.addListener(SWT.Selection, new Listener() {



                public void handleEvent(Event e) {

                    windowControl.openCheckControlCenter();

                }

            });

        }

        if (system.hasAccess("Importa��o de Extrato")) {

            MenuItem importExtractBankDialog = new MenuItem(financialMenu, SWT.NONE);

            importExtractBankDialog.setText("Importa��o de Extrato Banc�rio");

            importExtractBankDialog.addListener(SWT.Selection, new Listener() {



                public void handleEvent(Event e) {

                    windowControl.openLinkAccountBankControlCenter();

                }

            });

        }

        if (financialMenu.getItemCount() == 0) financialInfoMenuItem.dispose();
