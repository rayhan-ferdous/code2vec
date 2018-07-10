    private void handleSendableEvent(JGCSSendableEvent event) {

        if (event.getDir() == Direction.UP) {

            mailbox.add(event);

            try {

                event.go();

            } catch (AppiaEventException e) {

                e.printStackTrace();

            }

        }

    }
