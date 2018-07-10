                    try {

                        stmt = conn.createStatement();

                        stmt.executeUpdate(sqlStmt);

                    } catch (SQLException e) {

                        status.setCode(GramJob.STATUS_FAILED);

                        status.setMessage("Cannot update job output database after finish - " + e.getMessage());

                        logger.error(e);

                        try {

                            updateStatusInDatabase(jobID, status);

                        } catch (SQLException se) {

                            logger.error(se);

                        }

                        synchronized (status) {

                            status.notifyAll();

                        }

                        return;

                    }

                    OutputFileType[] outputFile = outputs.getOutputFile();
