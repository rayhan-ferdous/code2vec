            if (cursor != null) {

                try {

                    cursor.close();

                } catch (SQLException ignored) {

                }

            }

            if (preparedStatement != null) {

                try {

                    preparedStatement.close();

                } catch (SQLException ignored) {

                }

            }

        }
