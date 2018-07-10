    public QueryHandeler(QueryIDE qIde) {

        action = null;

        updateCount = 0;

        resultsAvailable = false;

        metaAvailable = false;

        planAvailable = false;

        ide = qIde;

        lockConn = null;

        lockPoint = null;

    }
