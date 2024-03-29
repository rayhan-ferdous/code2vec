    public List<Threads> findThreadsByHql(String hql, int start, int maxrow) {

        Transaction tr = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            Query query = session.createQuery(hql);

            query.setFirstResult(start);

            query.setMaxResults(maxrow);

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
