    public void newProperties(PropertySheet ps) throws PropertyException {

        logger = ps.getLogger();

        propsFile = ps.getString(PROP_PROPERTIES_FILE, PROP_PROPERTIES_FILE_DEFAULT);

        logMath = (LogMath) ps.getComponent(PROP_LOG_MATH, LogMath.class);

        unitManager = (UnitManager) ps.getComponent(PROP_UNIT_MANAGER, UnitManager.class);

        binary = ps.getBoolean(PROP_IS_BINARY, getIsBinaryDefault());

        sparseForm = ps.getBoolean(PROP_SPARSE_FORM, getSparseFormDefault());

        vectorLength = ps.getInt(PROP_VECTOR_LENGTH, getVectorLengthDefault());

        model = ps.getString(PROP_MODEL, getModelDefault());

        dataDir = ps.getString(PROP_DATA_LOCATION, getDataLocationDefault()) + "/";

        distFloor = ps.getFloat(PROP_MC_FLOOR, PROP_MC_FLOOR_DEFAULT);

        mixtureWeightFloor = ps.getFloat(PROP_MW_FLOOR, PROP_MW_FLOOR_DEFAULT);

        varianceFloor = ps.getFloat(PROP_VARIANCE_FLOOR, PROP_VARIANCE_FLOOR_DEFAULT);

        useCDUnits = ps.getBoolean(PROP_USE_CD_UNITS, PROP_USE_CD_UNITS_DEFAULT);

    }
