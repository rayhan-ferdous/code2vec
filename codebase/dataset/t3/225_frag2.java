    public SymptomListDocument getSymptomList() {

        SymptomListDocument doc = null;

        ResultSet rs = null;

        try {

            helper = new DBHelper();

            rs = helper.getResultSet(SQL.getSymptomList());

            doc = SymptomListDocument.Factory.newInstance();

            doc.addNewSymptomList();

            while (rs.next()) {

                Symptom s = doc.getSymptomList().addNewSymptom();

                s.setDsm(rs.getString("GROUPDESCRIPTION"));

                s.setSymptom(rs.getString("SYMPTOM"));

                s.setSymptomno(rs.getString("SYMPTOMNO"));

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {

                    rs.close();

                }

                if (helper != null) {

                    helper.cleanup();

                }

            } catch (SQLException ee) {

                ee.printStackTrace();

            }

        }

        return doc;

    }
