        for (int i = 0; i < fields.length; i++) if (fields[i].startsWith(field)) return i;

        throw new IOException("Requested field " + field + " is not present in index field.");

    }



    private String[] getFieldsFromLine(String line) {

        return line.split("[" + ArgosDefaults.DATA_SEPARATOR + "]");
