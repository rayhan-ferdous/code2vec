            update(entityManagerFactory);

            System.out.println("*** query ***");

            query(entityManagerFactory);

            System.out.println("*** shuffle ***");

            shuffle(entityManagerFactory);

            System.out.println("*** query ***");

            query(entityManagerFactory);

            System.out.println("*** delete ***");

            delete(entityManagerFactory);

        } finally {

            entityManagerFactory.close();

            System.out.println("*** finished ***");

        }

    }
