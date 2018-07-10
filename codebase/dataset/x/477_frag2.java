    public List<Profilefields> findAllProfilefields() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tr = null;

        try {

            tr = session.beginTransaction();

            Query query = session.createQuery("from Profilefields");

            List<Profilefields> profileList = query.list();

            tr.commit();

            return profileList;

        } catch (HibernateException e) {

            if (tr != null) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return null;

    }
