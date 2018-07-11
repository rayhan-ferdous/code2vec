    private void handleRSE(RegisterSocketEvent ev) {

        try {

            ev.go();

        } catch (AppiaEventException e) {

            e.printStackTrace();

        }

    }
