    public boolean deleteProjects(Projects projects) {

        Session session = null;

        Transaction tr = null;

        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            session.delete(projects);

            tr.commit();

            return true;

        } catch (Exception e) {

            if (tr != null) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return false;

    }
