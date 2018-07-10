    private static void update(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("SELECT p FROM Person p");

            Collection<Person> collection = (Collection<Person>) query.getResultList();

            for (Person person : collection) {

                Address address = new Address();

                address.setCity("Addison");

                address.setStreet("Preston Road 1121");

                address.setPerson(person);

                person.getAddresses().add(0, address);

            }

            entityManager.getTransaction().commit();

        } finally {

            if (entityManager.getTransaction().isActive()) {

                entityManager.getTransaction().rollback();

            }

            entityManager.close();

        }

    }
