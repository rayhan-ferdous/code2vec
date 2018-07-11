    public void printMe() {

        PrinterJob printJob = PrinterJob.getPrinterJob();

        HashPrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();

        if (printJob.printDialog(pSet)) {

            printJob.setPrintable(this);

            try {

                printJob.print(pSet);

            } catch (Exception PrintException) {

            }

        }

    }
