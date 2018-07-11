    protected void prepSelectResultSet(DBObject baseObject, String fieldName, DBConnection theConnection) throws DBException {

        FastStringBuffer prepStatement = FastStringBuffer.getInstance();

        try {

            prepStatement.append("SELECT ");

            prepStatement.append(fieldName);

            prepStatement.append(" from ");

            prepStatement.append(baseObject.getJDBCMetaData().getTargetTable());

            String whereClause = baseObject.buildWhereClause(false);

            prepStatement.append(whereClause);

            String thePrepString = prepStatement.toString();

            if (log.isDebugEnabled()) {

                log.debug("Preparing prepared statement:  " + thePrepString);

            }

            PreparedStatement prep = theConnection.createPreparedStatement(thePrepString);

            if (prep == null) {

                throw new DBException("Unable to create prepared statement for CLOB retrieval." + "  Check DBConnection log for details");

            }

            theConnection.execute();

            if (log.isDebugEnabled()) {

                log.debug("Succesfully executed prepared statement");

            }

        } finally {

            prepStatement.release();

            prepStatement = null;

        }

    }
