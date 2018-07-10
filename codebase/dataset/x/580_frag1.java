    int getRealByteCount() throws SQLException {

        DataPage dummy = index.getDatabase().getDataPage();

        int len = pageData.size();

        int size = 2 + DataPage.LENGTH_INT * (len + 1);

        for (int i = 0; i < len; i++) {

            SearchRow row = pageData.get(i);

            size += getRowSize(dummy, row);

        }

        size += index.getRecordOverhead();

        cachedRealByteCount = size;

        return size;

    }
