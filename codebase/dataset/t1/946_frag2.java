    public void storePatch(Patch p, int bankNum, int patchNum) {

        try {

            Thread.sleep(100);

        } catch (Exception e) {

        }

        p.sysex[3] = (byte) 0x16;

        p.sysex[6] = (byte) bankNum;

        p.sysex[7] = (byte) patchNum;

        sendPatchWorker(p);

        try {

            Thread.sleep(100);

        } catch (Exception e) {

        }

    }
