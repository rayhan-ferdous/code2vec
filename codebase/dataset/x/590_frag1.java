    public void updateWorkQueueItemToInProgress(long id) {

        logger.debug("updateWorkQueueItemToInProgress(%s)", id);

        Connection connection = null;

        NamedParameterStatement statement = null;

        try {

            connection = this.dataSource.getConnection();

            statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS));

            statement.setLong(QueryStore.UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS_PARAM_ID, id);

            statement.setInt(QueryStore.UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS_PARAM_STATE, WorkQueueItem.STATE_IN_PROGRESS);

            statement.executeUpdate();

        } catch (Exception e) {

            logger.error(e.getMessage(), e);

            throw new ApplicationException(e.getMessage());

        } finally {

            close(statement);

            close(connection);

        }

    }
