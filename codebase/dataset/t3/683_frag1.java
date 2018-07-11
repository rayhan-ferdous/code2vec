    public List<Smilies> getSmilies() {

        Transaction tr = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            Query query = session.createQuery("from Smilies as s where s.type='smiley' order by s.displayorder");

            List<Smilies> list = query.list();

            tr.commit();

            return list;

        } catch (HibernateException he) {

            if (tr != null) {

                tr.rollback();

                tr = null;

            }

            he.printStackTrace();

        }

        return null;

    }
