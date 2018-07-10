    public void executeRead() {

        Session session = null;

        Transaction tx = null;

        try {

            session = sessionFactory.openSession();

            tx = session.beginTransaction();

            executionResults = session.get(targetClass, (Serializable) parameters.get(DataConnector.RECORD_KEY_PARAMETER));

            if (executionResults != null) {

                responseCode = 0;

                responseString = "Execution complete";

            } else {

                responseCode = 1;

                responseString = "Record not found";

            }

            tx.commit();

        } catch (Throwable t) {

            responseCode = 10;

            responseString = t.toString();

            t.printStackTrace();

            if (tx != null) {

                try {

                    tx.rollback();

                } catch (Throwable t2) {

                    t2.printStackTrace();

                }

            }

        } finally {

            if (session != null) {

                try {

                    session.close();

                } catch (Throwable t2) {

                    t2.printStackTrace();

                }

            }

        }

    }
