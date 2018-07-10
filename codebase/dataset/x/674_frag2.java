    public boolean deleteTags(Tags tags) {

        Transaction tr = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            session.delete(tags);

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
