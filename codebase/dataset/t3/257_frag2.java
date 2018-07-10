    public static double getCurrentBalance(long aid) throws Exception {

        Session s = null;

        try {

            s = HibernateUtils.getSessionFactory().getCurrentSession();

            s.beginTransaction();

            String query = "select R.currentBalance from Account R where R.accountId=?";

            Query q = s.createQuery(query);

            q.setLong(0, aid);

            double aname = (Double) q.uniqueResult();

            s.getTransaction().commit();

            return aname;

        } catch (Exception e) {

            throw e;

        } finally {

            if (s != null) HibernateUtils.closeSession();

        }

    }
