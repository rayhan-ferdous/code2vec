    private static void attach(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Group group = entityManager.merge(detachedGroup);

            entityManager.getTransaction().commit();

            System.out.println(group);

        } catch (OptimisticLockException e) {

            e.printStackTrace(System.out);

        } finally {

            if (entityManager.getTransaction().isActive()) {

                entityManager.getTransaction().rollback();

            }

            entityManager.close();

        }

    }
