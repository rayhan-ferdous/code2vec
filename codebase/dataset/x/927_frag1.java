    private XSPFTrackInfo getNextTrack() {

        try {

            if (mPlaylist.size() == 0) return null;

            if (mNextPlaylistItem >= mPlaylist.size()) {

                mPlaylist = getPlaylist();

                mNextPlaylistItem = 1;

                return mPlaylist.get(0);

            } else {

                mNextPlaylistItem++;

                if (mNextPlaylistItem == mPlaylist.size() - 1) updatePlaylistDelayed();

                return mPlaylist.get(mNextPlaylistItem - 1);

            }

        } catch (NullPointerException e) {

            return null;

        }

    }
