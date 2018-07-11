            List result = em.createQuery("SELECT P.firstName FROM " + Person.class.getName() + " P WHERE false = True").getResultList();

            assertEquals(0, result.size());

            tx.rollback();

        } finally {

            if (tx.isActive()) {

                tx.rollback();

            }

            em.close();
