    public static final String getSubfieldData(DataField df, char code) {

        String result = null;

        if (df != null) {

            Subfield sf = df.getSubfield(code);

            if (sf != null && sf.getData() != null) {

                result = sf.getData();

            }

        }

        return result;

    }
