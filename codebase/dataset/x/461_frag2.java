    public void play() {

        notesPlaying = 0;

        tick = 0;

        Ticker.setTickListener(this);

        Ticker.setTempo(120);

        Ticker.start();

    }
