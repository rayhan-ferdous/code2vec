    public List<Transaction> getChildTransactions(long uid, long txId) throws Exception {

        Session s = null;

        try {

            s = HibernateUtils.getSessionFactory().getCurrentSession();

            s.beginTransaction();

            String query = "select R from Transaction R where R.initiatorId=? and R.parentTxId=?";

            Query q = s.createQuery(query);

            q.setLong(0, uid);

            q.setLong(1, txId);

            return q.list();

        } catch (Exception e) {

            throw e;

        } finally {

            if (s != null) HibernateUtils.closeSession();

        }

    }
