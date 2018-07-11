    private void flush(boolean laterToo) throws InterruptedException {

        SnapshotImpl s;

        synchronized (this) {

            if (flusher != null) {

                flusher.interrupt();

                flusher = null;

            }

            if (unwrittenSnapshot != null) {

                acquireWriteLock();

                s = unwrittenSnapshot;

                unwrittenSnapshot = null;

            } else {

                if (laterToo && writeLock != null) deferNextCommit = false;

                return;

            }

        }

        if (s != null) s.save();

        releaseWriteLock();

    }
