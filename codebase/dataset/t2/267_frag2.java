    public KorgWavestationPerformanceMapDriver(final Device device) {

        super(device, "Performance Map", "Gerrit Gehnen");

        sysexID = "F0423*285D";

        sysexRequestDump = new SysexHandler("F0 42 @@ 28 07 F7");

        trimSize = 521;

        patchNameStart = 0;

        patchNameSize = 0;

        deviceIDoffset = 0;

        checksumStart = 5;

        checksumEnd = 518;

        checksumOffset = 519;

    }
