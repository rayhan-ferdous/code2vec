    public void setAlbum(String albumLoc) {

        albumLoc = testTrack;

        tracksUris = new ArrayList<String>();

        File trackFolder = new File(albumLoc);

        if (trackFolder.isDirectory()) {

            File[] albumtracks = trackFolder.listFiles();

            for (File element : albumtracks) {

                tracksUris.add(element.getAbsolutePath());

            }

            currentTrack = 0;

            playPlease = true;

        }

    }
