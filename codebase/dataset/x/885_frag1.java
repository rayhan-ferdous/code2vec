            executeBatch = true;

        }

        rs.close();

        if (executeBatch) {

            pss.stm_delServiceLanguage.executeBatch();

            pss.stm_delServiceOntology.executeBatch();

            pss.stm_delServiceProtocol.executeBatch();

            pss.stm_delServiceProperty.executeBatch();

            pss.stm_delService.executeBatch();
