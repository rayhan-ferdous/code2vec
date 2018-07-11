        if ((level & 0xFFFFFF00) != 0) throw new IllegalArgumentException("'level' out of range!");

        byte[] aPDU = TFrame.getNew(6);

        aPDU[TFrame.APDU_START + 0] = (byte) (apci >> 24);

        aPDU[TFrame.APDU_START + 1] = (byte) (apci >> 16);

        aPDU[TFrame.APDU_START + 2] = (byte) level;

        aPDU[TFrame.APDU_START + 3] = (byte) (key >> 24);
