    public static synchronized CsdRenderResult generateCSD(BlueData data, float startTime, float endTime, boolean isRealTime) throws SoundObjectException {

        ParameterHelper.clearCompilationVarNames(data);

        boolean usingAPI = isRealTime && APIUtilities.isCsoundAPIAvailable() && GeneralSettings.getInstance().isUsingCsoundAPI();

        float renderStartTime = data.getRenderStartTime();

        Tables tables = (Tables) data.getTableSet().clone();

        ArrayList originalParameters;

        originalParameters = ParameterHelper.getAllParameters(data.getArrangement(), data.getMixer());

        assignParameterNames(originalParameters);

        Arrangement arrangement = (Arrangement) data.getArrangement().clone();

        arrangement.clearUnusedInstrAssignments();

        boolean hasInstruments = arrangement.size() > 0;

        PolyObject tempPObj = (PolyObject) data.getPolyObject().clone();
