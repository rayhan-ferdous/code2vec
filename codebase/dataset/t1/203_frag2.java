    public RolandMT32TimbreTempBankDriver() {

        super("Timbre Temp Bank", "Fred Jan Kraan", NS, 4);

        sysexID = "F041**16";

        deviceIDoffset = 0;

        bankNumbers = new String[] { "" };

        patchNumbers = new String[2 * 4];

        System.arraycopy(DriverUtil.generateNumbers(1, 8, "##"), 0, patchNumbers, 0, 8);

        singleSysexID = "F041**16";

        singleSize = HSIZE + SSIZE + 1;

        patchSize = HSIZE + SSIZE * NS + 1;

    }
