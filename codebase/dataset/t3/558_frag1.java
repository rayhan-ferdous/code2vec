    private static void update(SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();

        try {

            session.beginTransaction();

            Query query = session.createQuery("FROM Person");

            Collection<Person> list = (Collection<Person>) query.list();

            for (Person person : list) {

                person.setFirstName("Carl");

                Address address = new Address();

                address.setCity("Austin");

                address.setStreet("Silver Avenue 21");

                person.setAddress(address);

            }

            session.getTransaction().commit();

        } finally {

            if (session.getTransaction().isActive()) {

                session.getTransaction().rollback();

            }

            session.close();

        }

    }
