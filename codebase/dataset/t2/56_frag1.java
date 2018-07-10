    public boolean modifyValidating(Validating validating) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tr = null;

        try {

            tr = session.beginTransaction();

            session.update(validating);

            tr.commit();

            return true;

        } catch (HibernateException e) {

            if (tr != null) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return false;

    }
