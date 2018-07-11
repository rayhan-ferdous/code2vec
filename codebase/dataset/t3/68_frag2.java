    public ArrayList<String> getArtistTracks(String artist) {

        ArrayList<String> albums = new ArrayList<String>();

        ResultSet rs = null;

        try {

            int artistId = getArtistId(artist);

            String query = "SELECT filename " + "FROM tracks_table, artists_table " + "WHERE tracks_table.artist_id=artists_table.id AND " + "tracks_table.artist_id=?";

            PreparedStatement st = conn.prepareCall(query);

            st.setInt(1, artistId);

            rs = st.executeQuery();

            while (rs.next()) {

                String album = rs.getString(1);

                albums.add(album);

            }

            st.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return albums;

    }
