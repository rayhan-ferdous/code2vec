    public boolean deleteUsergroups(Usergroups usergroup) {

        Session session = null;

        Transaction tr = null;

        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            session.delete(usergroup);

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
