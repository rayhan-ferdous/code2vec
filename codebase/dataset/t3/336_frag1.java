    public Subject findByAlias(Integer subjectAlias) throws SelectException, DBConnectionException {

        Subject subject = null;

        Statement stmt = null;

        try {

            stmt = OracleJDBConnector.getInstance().getStatement();

        } catch (XmlIOException e1) {

            e1.printStackTrace();

            throw new DBConnectionException("Unable to Get Statement", e1);

        }

        Criteria critWhere = new Criteria();

        critWhere.addCriterion("SUBJECT_ALIAS", subjectAlias);

        try {

            ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, critWhere).toString());

            if (result != null) {

                while (result.next()) {

                    subject = new Subject();

                    subject.setName(result.getString("SUBJECT_NAME"));

                    subject.setDescription(result.getString("SUBJECT_DESCRIPTION"));

                    subject.setId(result.getInt("SUBJECT_ID"));

                    subject.setCoeff(result.getFloat("SUBJECT_COEFFICIENT"));

                    subject.setAlias(result.getString("SUBJECT_ALIAS"));

                    subject.setTeachingUnit(null);

                }

            }

            stmt.close();

        } catch (SQLException e) {

            e.printStackTrace();

            throw new SelectException(TABLE_NAME + " Request Error", e);

        }

        return subject;

    }
