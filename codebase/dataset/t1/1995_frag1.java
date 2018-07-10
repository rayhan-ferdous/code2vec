            try {

                if (callableStatement != null) callableStatement.close();

            } catch (SQLException e) {

                logger.warn("cannot close callable statement", e);

            }

            try {

                if (connection != null) connection.close();

            } catch (SQLException e) {

                logger.warn("cannot close connection", e);

            }

        }
