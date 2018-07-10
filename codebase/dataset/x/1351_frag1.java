    public void processPause(int modifiers) {

        if (playerState == PLAY) {

            try {

                player.pause();

            } catch (BasicPlayerException e) {

            }

            playerState = PAUSE;

            changePlayPauseState(playerState);

        } else if (playerState == PAUSE) {

            try {

                player.resume();

            } catch (BasicPlayerException e) {

            }

            playerState = PLAY;

            changePlayPauseState(playerState);

        }

    }
