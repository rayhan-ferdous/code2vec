    public Usergroups findUserGroupById(Short userGroupId) {

        Session session = null;

        Transaction tr = null;

        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            Usergroups userGroup = (Usergroups) session.get(Usergroups.class, userGroupId);

            tr.commit();

            return userGroup;

        } catch (Exception e) {

            if (tr != null && tr.isActive()) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return null;

    }
