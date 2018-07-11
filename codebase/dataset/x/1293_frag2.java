    public void stopAbletonClipUpdaters() {

        if (this.abletonMIDIClipUpdater != null) {

            this.abletonMIDIClipUpdater.stop();

            this.abletonMIDIClipUpdater = null;

        }

        if (this.abletonOSCClipUpdater != null) {

            this.abletonOSCClipUpdater.stop();

            this.abletonOSCClipUpdater = null;

        }

    }
