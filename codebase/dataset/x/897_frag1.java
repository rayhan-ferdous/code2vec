        usedSpaceStream = createUsedSpaceStream();

        objectsStream = createObjectsStream();

        rootsStream = createRootsStream();

        refFromImmortalStream = createRefFromImmortalStream();

        serverSpace.resize(0);

        resetData();

    }



    /**

   * Get the name of this driver type.

   * @return The name, "MMTk TreadmillDriver" for this driver.

   */

    protected String getDriverName() {

        return "MMTk TreadmillDriver";

    }



    @Interruptible

    private IntStream createUsedSpaceStream() {

        return VM.newGCspyIntStream(this, "Used Space stream", 0, blockSize, 0, 0, "Space used: ", " bytes", StreamConstants.PRESENTATION_PERCENT, StreamConstants.PAINT_STYLE_ZERO, 0, Color.Red, true);
