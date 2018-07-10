    private boolean getSparseFormDefault() {

        loadProperties();

        String sparse = (String) properties.get(PROP_SPARSE_FORM);

        if (sparse != null) {

            return (Boolean.valueOf(binary).equals(Boolean.TRUE));

        } else {

            return PROP_SPARSE_FORM_DEFAULT;

        }

    }
