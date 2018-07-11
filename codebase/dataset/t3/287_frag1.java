    public static Any getNMR(Connection con, Any values, Any retValue) throws SQLException {

        String sSql = ResourceBundle.getBundle(Documents.class.getName()).getString("getNMR");

        PreparedStatement stmt = con.prepareStatement(sSql);

        ResultSet rSet = stmt.executeQuery();

        ArrayList<HashMap> arMaps = new ArrayList<HashMap>();

        while (rSet.next()) {

            HashMap map = new HashMap();

            map.put("1", rSet.getString("CAUTONUM"));

            map.put("2", rSet.getString("CNUMMASK"));

            map.put("3", rSet.getString("ILASTNUM"));

            arMaps.add(map);

        }

        rSet.close();

        stmt.close();

        retValue.insert_Value(arMaps.toArray(new HashMap[arMaps.size()]));

        return retValue;

    }
