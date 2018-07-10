    public List getChildrenAccountCategories(long uid, long catid) throws Exception {

        Session s = null;

        try {

            s = HibernateUtils.getSessionFactory().getCurrentSession();

            s.beginTransaction();

            String query = "select R from AccountCategory R where R.uid=? and R.parentCategoryId=? order by R.categoryId";

            Query q = s.createQuery(query);

            q.setLong(0, uid);

            q.setLong(1, catid);
