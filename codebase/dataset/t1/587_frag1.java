        String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + "cnv2_hg" + build + "?autoReconnect=true";

        boolean err = false;

        try {

            conn = connect(dbUser, dbPassword, url);

            String query = "select chr, start, end, frequency from cnvblock";

            prestmt = conn.prepareStatement(query);

            resultSet = prestmt.executeQuery();

            int rowCount = 0;

            while (resultSet.next()) {
