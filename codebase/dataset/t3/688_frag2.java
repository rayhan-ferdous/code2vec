    public List<Attachments> findAttchmentsByJs(String hql, int startrow, int maxrow) {

        List<Attachments> attlist = new ArrayList<Attachments>();

        Transaction tr = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            tr = session.beginTransaction();

            Query query = session.createQuery(hql);

            query.setFirstResult(startrow);

            query.setMaxResults(maxrow);

            List<Attachments> list = query.list();

            tr.commit();

            Iterator it = list.iterator();

            while (it.hasNext()) {

                Object[] os = (Object[]) it.next();

                Attachments a = (Attachments) os[0];

                Threads t = (Threads) os[1];

                attlist.add(a);

            }

            return attlist;

        } catch (HibernateException e) {

            if (tr != null) {

                tr.rollback();

            }

            e.printStackTrace();

        }

        return null;

    }
