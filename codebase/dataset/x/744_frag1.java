    public void setEndOfTransfer() {

        try {

            checkFtpTransferStatus();

        } catch (FtpNoTransferException e) {

            return;

        }

    }
