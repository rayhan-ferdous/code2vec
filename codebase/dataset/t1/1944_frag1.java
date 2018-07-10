        final String dbuser = "guest2";

        final String dbpasswd = "";

        Connection con = null;

        Statement statement = null;

        try {

            Class.forName(dbdriver);

            con = DriverManager.getConnection(dbconnect, dbuser, dbpasswd);

            statement = con.createStatement();

        } catch (Exception e) {

            System.out.println("FATAL: cant load the database driver <" + dbdriver + ">!");

            System.exit(1);

        }

        String wdbInit = "SELECT wci.begin('" + dbuser + "')";
