    public void storePatch(Patch p, int bankNum, int patchNum) {

        try {

            Thread.sleep(100);

        } catch (Exception e) {

        }

        p.sysex[3] = (byte) 0x21;

        p.sysex[6] = (byte) (bankNum << 1);

        p.sysex[7] = (byte) 0x40;

        sendPatchWorker(p);

        try {

            Thread.sleep(100);

        } catch (Exception e) {

        }

    }
