    public void addShares(Set sharees_, PXPerson sharer_) {

        if ((sharees_ != null) && (sharees_.size() > 0) && (sharer_ != null)) {

            Transaction transaction = null;

            try {

                Session session = PXObjectStore.getInstance().getThreadSession();

                Iterator shareeIterator = sharees_.iterator();

                transaction = session.beginTransaction();

                while (shareeIterator.hasNext()) {

                    PXIdentity sharee = (PXIdentity) shareeIterator.next();

                    this.addShare(sharee, sharer_);

                }

                transaction.commit();

            } catch (Exception e) {

                LOG.debug(null, e);

                if (transaction != null) {

                    try {

                        transaction.rollback();

                    } catch (Exception f) {

                        LOG.warn(f);

                    }

                }

            }

        }

    }
