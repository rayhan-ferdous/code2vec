    public void moveOutOfBoundsBeatsToNewMeasure(long start) {

        Iterator it = getSong().getTracks();

        while (it.hasNext()) {

            TGTrack track = (TGTrack) it.next();

            getTrackManager().moveOutOfBoundsBeatsToNewMeasure(track, start);

        }

    }
