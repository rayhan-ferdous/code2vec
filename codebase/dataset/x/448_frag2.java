    public void stop() {

        stopMe = true;

        if (capture != null) {

            capture.abort();

            capture = null;

        }

        if (exporter != null) {

            exporter.abort();

            exporter = null;

        }

        if (process != null) {

            process.destroy();

        }

        stopMe = false;

        stopped = true;

    }
