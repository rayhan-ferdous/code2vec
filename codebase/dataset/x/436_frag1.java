    public void processWindowEvent(WindowEvent e) {

        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {

            instance = null;

        }

        if (!IJ.isMacro()) ignoreInterrupts = false;

    }
