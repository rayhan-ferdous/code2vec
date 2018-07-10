    private void faderInEnable(boolean enable) {

        if (enable) {

            trns = MidiUtil.getTransmitter(AppConfig.getFaderPort());

            rcvr = new FaderReceiver();

            trns.setReceiver(rcvr);

        } else {

            if (trns != null) trns.close();

            if (rcvr != null) rcvr.close();

        }

    }
