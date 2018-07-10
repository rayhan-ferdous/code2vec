        useCDUnits = ps.getBoolean(PROP_USE_CD_UNITS, PROP_USE_CD_UNITS_DEFAULT);

    }



    private void loadProperties() {

        if (properties == null) {

            properties = new Properties();

            try {

                URL url = getClass().getResource(propsFile);

                properties.load(url.openStream());

            } catch (IOException ioe) {

                ioe.printStackTrace();

            }

        }

    }



    /**

     * Returns whether the models are binary by default

     *

     * @return true if the models are binary by default

     */

    private boolean getIsBinaryDefault() {

        loadProperties();

        String binary = (String) properties.get(PROP_IS_BINARY);

        if (binary != null) {

            return (Boolean.valueOf(binary).equals(Boolean.TRUE));

        } else {

            return PROP_IS_BINARY_DEFAULT;

        }

    }



    /**

     * Returns whether the matrices are in sparse form by default.

     *

     * @return true if the matrices are in sparse form by default

     */

    private boolean getSparseFormDefault() {

        loadProperties();

        String sparse = (String) properties.get(PROP_SPARSE_FORM);

        if (sparse != null) {

            return (Boolean.valueOf(binary).equals(Boolean.TRUE));

        } else {

            return PROP_SPARSE_FORM_DEFAULT;

        }

    }



    /**

     * Returns the default vector length.

     */

    private int getVectorLengthDefault() {

        loadProperties();

        String length = (String) properties.get(PROP_VECTOR_LENGTH);

        if (length != null) {

            return Integer.parseInt(length);

        } else {

            return PROP_VECTOR_LENGTH_DEFAULT;

        }

    }



    /**

     * Returns the default model definition file.

     *

     * @return the default model definition file

     */

    private String getModelDefault() {

        loadProperties();

        String mdef = (String) properties.get(PROP_MODEL);

        if (mdef != null) {

            return mdef;

        } else {

            return PROP_MODEL_DEFAULT;

        }

    }



    /**

     * Returns the default data location.

     *

     * @return the default data location

     */

    private String getDataLocationDefault() {

        loadProperties();

        String location = (String) properties.get(PROP_DATA_LOCATION);

        if (location != null) {

            return location;

        } else {

            return PROP_DATA_LOCATION_DEFAULT;

        }

    }



    public String getName() {

        return name;

    }



    public void load() throws IOException {

        hmmManager = new HMMManager();

        contextIndependentUnits = new LinkedHashMap();
