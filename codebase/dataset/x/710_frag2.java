    public static void closeStatement(Statement statement) {

        if (statement == null) return;

        try {

            statement.close();

        } catch (SQLException e) {

        }

    }
