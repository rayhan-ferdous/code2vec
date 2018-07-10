    private static void insert(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Person person = new Person();

            person.setFirstName("Jesse");

            person.setLastName("James");

            person.setSalary(new BigDecimal("1234.56"));

            entityManager.persist(person);

            entityManager.getTransaction().commit();

        } finally {

            if (entityManager.getTransaction().isActive()) {

                entityManager.getTransaction().rollback();

            }

            entityManager.close();

        }

    }
