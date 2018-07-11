    public void requestPatchDump(int bankNum, int patchNum) {

        int location = patchNum;

        int opcode = QSConstants.OPCODE_MIDI_USER_PROG_DUMP_REQ;

        if (location > QSConstants.MAX_LOCATION_PROG) {

            location -= (QSConstants.MAX_LOCATION_PROG + 1);

            opcode = QSConstants.OPCODE_MIDI_EDIT_PROG_DUMP_REQ;

        }

        send(sysexRequestDump.toSysexMessage(getChannel(), new SysexHandler.NameValue("opcode", opcode), new SysexHandler.NameValue("patchNum", location)));

    }



    /**

   * Sends a patch to the synth's edit buffer.

   * @param p the patch to send to the edit buffer

   */

    public void sendPatch(Patch p) {

        storePatch(p, 0, QSConstants.MAX_LOCATION_PROG + 1);

    }



    /**

   * Sends a patch to a set location on a synth.  See comment for requestPatchDump for

   * explanation of patch numbers > 127.  We save the old values, then set the

   * opcode and target location, then send it, then restore the old values

   * @param p the patch to send

   * @param bankNum ignored - you can only send to the User bank on Alesis QS synths

   * @param patchNum the patch number to send it to

   */

    public void storePatch(Patch p, int bankNum, int patchNum) {

        int location = patchNum;

        byte opcode = QSConstants.OPCODE_MIDI_USER_PROG_DUMP;

        byte oldOpcode = ((Patch) p).sysex[QSConstants.POSITION_OPCODE];

        byte oldLocation = ((Patch) p).sysex[QSConstants.POSITION_LOCATION];

        if (location > QSConstants.MAX_LOCATION_PROG) {

            location -= (QSConstants.MAX_LOCATION_PROG + 1);

            opcode = QSConstants.OPCODE_MIDI_EDIT_PROG_DUMP;

        }

        ((Patch) p).sysex[QSConstants.POSITION_OPCODE] = opcode;

        ((Patch) p).sysex[QSConstants.POSITION_LOCATION] = (byte) location;

        Logger.reportStatus(((Patch) p).sysex);

        sendPatchWorker(p);

        ((Patch) p).sysex[QSConstants.POSITION_OPCODE] = oldOpcode;

        ((Patch) p).sysex[QSConstants.POSITION_LOCATION] = oldLocation;

    }

}
