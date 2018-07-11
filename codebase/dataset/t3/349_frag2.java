    public List<Ranks> findAllRanks() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tr = null;

        try {

            tr = session.beginTransaction();

            Query query = session.createQuery("from Ranks order by postshigher");

            List<Ranks> list = query.list();

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
