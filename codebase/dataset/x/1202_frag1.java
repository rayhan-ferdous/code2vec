    private void dummyTrafficToggleButtonItemStateChanged(java.awt.event.ItemEvent evt) {

        if (dummyTrafficToggleButton.isSelected()) {

            String messageSizeString = messageSizeTextField.getText();

            int messageSize;

            try {

                messageSize = Integer.parseInt(messageSizeString);

            } catch (Exception e) {

                LOGGER.log(Level.SEVERE, null, e);

                showError(e.toString());

                return;

            }

            String delayString = delayTextField.getText();

            int delay;

            try {

                delay = Integer.parseInt(delayString);

            } catch (Exception e) {

                LOGGER.log(Level.SEVERE, null, e);

                showError(e.toString());

                return;

            }

            trafficShaperCoordinator.setPackageSize(messageSize);

            trafficShaperCoordinator.setDelay(delay);

            trafficShaperCoordinator.start();

        } else {

            trafficShaperCoordinator.stop();

        }

    }
