    public void restart() {

        try {

            if (!opened) openFile(openedFile, false);

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

        }

        status = Status.playing;

    }
