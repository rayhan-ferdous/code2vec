    public HashSet<AcademicYear> findAll() throws DBConnectionException, SelectException {

        HashSet<AcademicYear> acaYearList = null;

        Statement stmt = null;

        try {

            stmt = OracleJDBConnector.getInstance().getStatement();

        } catch (XmlIOException e1) {

            e1.printStackTrace();

            throw new DBConnectionException("Unable to Get Statement", e1);

        }

        try {

            ResultSet result = stmt.executeQuery(new SelectQuery(AcademicYearDAO.TABLE_NAME).toString());

            if (result != null) {

                acaYearList = new HashSet<AcademicYear>();

                while (result.next()) {

                    AcademicYear acaYear = new AcademicYear();

                    acaYear.setId(result.getInt("ACADEMIC_YEAR_ID"));

                    acaYear.setName(result.getString("ACADEMIC_YEAR_NAME"));

                    acaYearList.add(acaYear);

                }

            }

            stmt.close();

        } catch (SQLException e) {

            e.printStackTrace();

            throw new SelectException(TABLE_NAME + " Request Error", e);

        }

        return acaYearList;

    }
