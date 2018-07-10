    @Override

    public Collection<?> getFieldNames() throws SearchLibException {

        if (!online) throw new SearchLibException("Index is offline");

        rwl.r.lock();

        try {

            if (reader != null) return reader.getFieldNames();

            return null;

        } finally {

            rwl.r.unlock();

        }

    }
