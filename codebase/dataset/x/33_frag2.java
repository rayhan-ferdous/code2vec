    private void setStatus(MANAGERSTATUS newStatus) {

        boolean changed = false;

        MANAGERSTATUS oldStatus;

        synchronized (statusMutex) {

            oldStatus = status;

            if (newStatus != status) {

                status = newStatus;

                logger.info(status.getI18nDescription());

                changed = true;

            }

        }

        if (changed) {

            fireOPC2OutStatusChanged(oldStatus, newStatus);

        }

    }
