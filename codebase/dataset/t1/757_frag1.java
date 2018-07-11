    public static void changeDisplayType(String usrlogin, String tab, String displaytype) throws DbException {

        Db db = null;

        String sql = "";

        try {

            db = new Db();

            Statement stmt = db.getStatement();

            sql = "UPDATE tabs SET display_type = '" + displaytype + "' " + "WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";

            stmt.executeUpdate(sql);

        } catch (SQLException sqlex) {

            throw new DbException(sqlex.getMessage() + ": " + sql);

        } finally {

            if (db != null) db.close();

        }

    }
