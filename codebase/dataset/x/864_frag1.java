            super.commit(transaction);

        }

    }



    protected void rollback(Transaction transaction) {

        if (status != DELETED) {

            assertNotFloating();

            super.rollback(transaction);

        }

    }



    /** a shortcut method to mark this object as persistent */

    public final void makePersistent() {

        getTable().create(this);
