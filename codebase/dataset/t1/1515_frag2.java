    public void unsubscribe(List list) {

        try {

            Connection conn = Ozymandias.getDBConnection();

            Statement stmt = conn.createStatement();

            String dbQuery = "DELETE FROM UserLists WHERE (U_ID='" + getUID() + "' AND L_ID='" + list.getLID() + "')";

            stmt.executeQuery(dbQuery);

        } catch (SQLException e) {

            System.out.println(e);

        } catch (ClassNotFoundException e) {

            System.out.println(e);

        } catch (InstantiationException e) {

            System.out.println(e);

        } catch (IllegalAccessException e) {

            System.out.println(e);

        }

    }
