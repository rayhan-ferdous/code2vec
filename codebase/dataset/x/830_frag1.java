    private void enableButtons() {

        resetRbnbServer.setEnabled(true);

        selectArchiveRoot.setEnabled(true);

        startCaptureButton.setEnabled(true);

        captureHelpButton.setEnabled(true);

        startSendThread.setEnabled(segmentSelected());

    }
