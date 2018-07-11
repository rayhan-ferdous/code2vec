    private Vector getContextSQL(Connection db, String concept_id) throws Exception {

        Vector context = new Vector();

        String query = "SELECT getContextNameStemmed('" + concept_id + "')";

        ResultSet rs = executeQuery(db, query);

        rs.next();

        Array namesArray = rs.getArray(1);

        if (namesArray != null) {

            ResultSet names = rs.getArray(1).getResultSet();

            while (names.next()) {

                String name = names.getString(2);

                context.add(name);

            }

        }

        return context;

    }
