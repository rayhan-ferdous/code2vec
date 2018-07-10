    public static synchronized CsdRenderResult generateCSDForBlueLive(BlueData data) {

        ParameterHelper.clearCompilationVarNames(data);

        boolean usingAPI = APIUtilities.isCsoundAPIAvailable() && GeneralSettings.getInstance().isUsingCsoundAPI();

        float totalDur = 3600f;

        Tables tables = (Tables) data.getTableSet().clone();

        ArrayList originalParameters = null;

        if (usingAPI) {

            originalParameters = ParameterHelper.getAllParameters(data.getArrangement(), data.getMixer());

            assignParameterNames(originalParameters);

        }

        Arrangement arrangement = (Arrangement) data.getArrangement().clone();

        arrangement.clearUnusedInstrAssignments();

        String[] instrIds = arrangement.getInstrumentIds();

        PolyObject tempPObj = (PolyObject) data.getPolyObject().clone();
