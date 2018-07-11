    public IllnessListDocument getIllnessList() {

        IllnessListDocument doc = null;

        ResultSet rsList = null;

        try {

            helper = new DBHelper();

            doc = IllnessListDocument.Factory.newInstance();

            PreparedStatement psList = helper.prepareStatement(SQL.getIllnessList());

            rsList = psList.executeQuery();

            doc.addNewIllnessList();

            while (rsList.next()) {

                Illness i = doc.getIllnessList().addNewIllness();

                i.setIllnessno(rsList.getString("ILLNESSNO"));

                i.setIllness(rsList.getString("ILLNESS"));

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (rsList != null) {

                    rsList.close();

                }

                if (helper != null) {

                    helper.cleanup();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return doc;

    }
