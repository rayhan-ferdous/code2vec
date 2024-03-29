    public List<Attachments> getAttachmentListByTid(Integer tid) {

        Transaction transaction = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            transaction = session.beginTransaction();

            String hql = "FROM Attachments AS a WHERE a.tid=?";

            Query query = session.createQuery(hql);

            query.setInteger(0, tid);

            List<Attachments> list = query.list();

            transaction.commit();

            return list;

        } catch (Exception exception) {

            exception.printStackTrace();

            if (transaction != null) {

                transaction.rollback();

            }

            return null;

        }

    }
