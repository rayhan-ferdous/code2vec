    public void close() {

        try {

            mZos.finish();

            mZos.close();

        } catch (Exception ex) {

            mObserverCont.setError(ex.getMessage());

        } finally {

            mObserverCont.end();

        }

    }
