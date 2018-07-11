    public void register(String name, Registry registry) throws PropertyException {

        this.name = name;

        registry.register(PROP_LOG_MATH, PropertyType.COMPONENT);

        registry.register(PROP_UNIT_MANAGER, PropertyType.COMPONENT);

        registry.register(PROP_IS_BINARY, PropertyType.BOOLEAN);

        registry.register(PROP_SPARSE_FORM, PropertyType.BOOLEAN);

        registry.register(PROP_VECTOR_LENGTH, PropertyType.INT);

        registry.register(PROP_MODEL, PropertyType.STRING);

        registry.register(PROP_DATA_LOCATION, PropertyType.STRING);

        registry.register(PROP_PROPERTIES_FILE, PropertyType.STRING);

        registry.register("classeMllr", PropertyType.STRING);

        registry.register(PROP_MC_FLOOR, PropertyType.FLOAT);

        registry.register(PROP_MW_FLOOR, PropertyType.FLOAT);

        registry.register(PROP_VARIANCE_FLOOR, PropertyType.FLOAT);

        registry.register(PROP_USE_CD_UNITS, PropertyType.BOOLEAN);

    }
