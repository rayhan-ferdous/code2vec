    private final void getClipForChannel(int c, int sfxid) {

        Clip clip = this.cachedSounds.get(sfxid);

        boolean exists = false;

        if (clip != null) {

            exists = true;

            if (!clip.isActive()) {

                channels[c] = clip;

                return;

            }

        }

        DataLine.Info info = new DataLine.Info(Clip.class, DoomSound.DEFAULT_SAMPLES_FORMAT);

        try {

            clip = (Clip) AudioSystem.getLine(info);

        } catch (LineUnavailableException e) {

            e.printStackTrace();

        }

        try {

            clip.open(DoomSound.DEFAULT_SAMPLES_FORMAT, S_sfx[sfxid].data, 0, S_sfx[sfxid].data.length);

        } catch (LineUnavailableException e) {

            e.printStackTrace();

        }

        if (!exists) this.cachedSounds.put(sfxid, clip);

        channels[c] = clip;

    }
