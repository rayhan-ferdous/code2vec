    public List<E> getObjectFromNamedQuery(String namedQuery, Object... parametros) {

        EntityManager em = getEntityManager();

        EntityTransaction tx = em.getTransaction();

        int position = 1;

        List<E> array = new ArrayList<E>();

        try {

            tx.begin();

            Query q = em.createNamedQuery(namedQuery);

            for (Object parametro : parametros) {

                q.setParameter(position++, parametro);

            }

            tx.commit();

            array = q.getResultList();

        } finally {

            if (tx.isActive()) {

                tx.rollback();

            }

            em.close();

        }

        return array;

    }
