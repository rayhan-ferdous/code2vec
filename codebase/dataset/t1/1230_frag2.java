    private static void insert(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Person person = new Person();

            person.setFirstName("Jesse");

            person.setLastName("James");

            entityManager.persist(person);

            entityManager.getTransaction().commit();

        } finally {

            if (entityManager.getTransaction().isActive()) {

                entityManager.getTransaction().rollback();

            }

            entityManager.close();

        }

    }
