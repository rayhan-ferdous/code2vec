    public void putPatch(Patch bank, Patch p, int patchNum) {

        if (!canHoldPatch(p)) {

            JOptionPane.showMessageDialog(null, "This type of patch does not fit in to this type of bank.", "Error", JOptionPane.ERROR_MESSAGE);

            return;

        }

        System.arraycopy(((Patch) p).sysex, 8, ((Patch) bank).sysex, getPatchStart(patchNum), 131);

        calculateChecksum(bank);

    }
