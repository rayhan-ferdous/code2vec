    public synchronized void setEventBufferLength(int eventBufferLength) {

        if (eventBufferLength < 2) {

            eventBufferLength = 2;

        } else if (eventBufferLength > 1000) {

            eventBufferLength = 1000;

        }

        this.eventBufferLength = eventBufferLength;

        getPrefs().putInt("MultiLineClusterTracker.eventBufferLength", eventBufferLength);

        initBuffers();

    }
