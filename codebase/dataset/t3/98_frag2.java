    private static void query(SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();

        try {

            Query query = session.createQuery("from Person");

            Collection<Person> list = (Collection<Person>) query.list();

            for (Person person : list) {

                LOGGER.info("Found: " + person);

                LOGGER.info("  with address: " + person.getAddress());

            }

        } finally {

            session.close();

        }

    }
