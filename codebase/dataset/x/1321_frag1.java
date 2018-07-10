    public boolean addNewUser() {

        try {

            Connection conn = getDBConnection();

            Statement stmt = conn.createStatement();

            String dbQuery = "INSERT INTO Users (Email, FirstName, LastName, Username) VALUES ('" + getEmail() + "', '" + getFirstName() + "', '" + getLastName() + "', '" + getUsername() + "')";

            stmt.executeQuery(dbQuery);

            dbQuery = "SELECT COUNT(U_ID) FROM Users WHERE Email='" + getEmail() + "'";

            ResultSet results = stmt.executeQuery(dbQuery);

            int numRows = results.getInt("COUNT(U_ID)");

            if (numRows > 0) return true; else return false;

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
