    public _StatementTriggerTable getStatementTriggerTable(String databaseURL, QualifiedIdentifier tableName) throws DException {

        if (!isServerActive) throw new DException("DSE2023", null);

        _StatementTriggerDatabase StatementTriggerDatabase = statementTriggerSystem.getStatementTriggerDatabase(databaseURL);

        return StatementTriggerDatabase.getStatementTriggerTable(tableName);

    }



    public _DataDictionarySystem getDataDictionarySystem() throws DException {
