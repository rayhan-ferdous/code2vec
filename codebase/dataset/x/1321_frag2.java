    public boolean isAdmin() {

        try {

            Connection conn = getDBConnection();

            Statement stmt = conn.createStatement();

            String dbQuery = "SELECT COUNT(A_ID) FROM Admins WHERE U_ID='" + getUID() + "'";

            ResultSet results = stmt.executeQuery(dbQuery);

            if (results.getInt("COUNT(A_ID)") == 1) return true; else return false;

        } catch (SQLException e) {

            System.out.println(e);

        } catch (ClassNotFoundException e) {

            System.out.println(e);

        } catch (InstantiationException e) {

            System.out.println(e);

        } catch (IllegalAccessException e) {

            System.out.println(e);

        }

        return false;

    }
