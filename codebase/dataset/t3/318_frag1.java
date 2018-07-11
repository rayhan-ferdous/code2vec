    protected void prepSelectResultSet(JDBCDataObject baseObject, String fieldName, DBConnection theConnection) throws DataException {

        try {

            FastStringBuffer prepStatement = FastStringBuffer.getInstance();

            prepStatement.append("SELECT ");

            prepStatement.append(fieldName);

            prepStatement.append(" from ");

            prepStatement.append(baseObject.getJDBCMetaData().getTargetTable());

            String whereClause = JDBCUtil.getInstance().buildWhereClause(baseObject, false);

            prepStatement.append(whereClause);

            String thePrepString = prepStatement.toString();

            prepStatement.release();

            if (log.isDebugEnabled()) {

                log.debug("Preparing prepared statement:  " + thePrepString);

            }

            PreparedStatement prep = theConnection.createPreparedStatement(thePrepString);

            if (prep == null) {

                throw new DataException("Unable to create prepared statement for CLOB retrieval." + "  Check DBConnection log for details");

            }

            theConnection.execute();

            if (log.isDebugEnabled()) {

                log.debug("Succesfully executed prepared statement");

            }

        } catch (DBException ex) {

            throw new DataException("Error prepping SELECT ResultSet", ex);

        }

    }
