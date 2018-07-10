    private void reportStatus(String msg) {

        if (msg != null) {

            errStr = msg;

            System.out.println(msg);

            playbackMonitor.repaint();

        }

    }
