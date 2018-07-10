    public static void query(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            Query query = entityManager.createQuery("SELECT g FROM Group g");

            Collection<Group> collection = (Collection<Group>) query.getResultList();

            for (Group group : collection) {

                System.out.println("found: " + group);

                for (User user : group.getUsers()) {

                    System.out.println("  with: " + user);

                }

            }

        } finally {

            entityManager.close();

        }

    }
