    public boolean insertAccess(Access access) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tr = null;

        try {

            tr = session.beginTransaction();

            session.save(access);

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
