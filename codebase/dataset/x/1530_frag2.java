    public void close() {

        try {

            mJos.finish();

            mJos.close();

        } catch (Exception ex) {

            mObserverCont.setError(ex.getMessage());

        } finally {

            mObserverCont.end();

        }

    }
