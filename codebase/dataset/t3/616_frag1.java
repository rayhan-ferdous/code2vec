    private static void insertanddetach(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Person person = new Person();

        try {

            entityManager.getTransaction().begin();

            person.setFirstName("Jesse");

            person.setLastName("Clark");

            entityManager.persist(person);

            entityManager.getTransaction().commit();

        } finally {

            if (entityManager.getTransaction().isActive()) {

                entityManager.getTransaction().rollback();

            }

            entityManager.close();

        }

        detachedPerson = person;

    }
