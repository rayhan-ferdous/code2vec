    private static void saveOrder(final Order order) {

        Session sess = Lifecycle.getHibernateDataSource().createNewSession();

        Transaction trans = null;

        try {

            trans = sess.beginTransaction();

            sess.save(order);

            trans.commit();

        } catch (Exception e) {

            if (trans != null) {

                trans.rollback();

            }

            throw new PulseException(e.getLocalizedMessage(), e);

        } finally {

            sess.close();

        }

    }
