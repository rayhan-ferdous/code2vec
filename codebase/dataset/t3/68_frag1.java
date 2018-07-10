    public String getAlbumFirstTrack(String artist, String album) {

        String firstTrack = "";

        ResultSet rs = null;

        try {

            int artistId = getArtistId(artist);

            int albumId = getAlbumId(artist, album);

            String query = "SELECT TOP 1 filename " + "FROM tracks_table " + "WHERE artist_id=? AND album_id=?";

            PreparedStatement st = conn.prepareStatement(query);

            st.setInt(1, artistId);

            st.setInt(2, albumId);

            rs = st.executeQuery();

            while (rs.next()) {

                firstTrack = rs.getString(1);

            }

            st.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return firstTrack;

    }
