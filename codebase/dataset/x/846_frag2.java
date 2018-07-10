            if (system.hasAccess("Extrato de Contas")) {

                MenuItem dailyAccountItem = new MenuItem(financialMenu, SWT.NONE);

                dailyAccountItem.setText("Extrato de Contas Caixa");

                dailyAccountItem.addListener(SWT.Selection, new Listener() {



                    public void handleEvent(Event e) {

                        windowControl.openExtractAccountReportControlCenter();

                    }

                });

            }

            if (system.hasAccess("Holerite")) {

                MenuItem simpleEmployeePaymentItem = new MenuItem(financialMenu, SWT.NONE);

                simpleEmployeePaymentItem.setText("Holerite");

                simpleEmployeePaymentItem.addListener(SWT.Selection, new Listener() {



                    public void handleEvent(Event e) {

                        windowControl.openHoleriteControlCenter();

                    }

                });

            }

            if (system.hasAccess("Gera��o de Boletos")) {
