    public List<Threads> findThreadsByUid(int uid) {

        Transaction tr = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            Query query = session.createQuery("from Threads as t where t.authorid=?");

            query.setParameter(0, uid);

            List<Threads> list = query.list();

            tr.commit();

            return list;

        } catch (HibernateException e) {

            if (tr != null) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return null;

    }
