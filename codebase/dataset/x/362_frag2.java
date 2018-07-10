    public boolean getLeaseActive() {

        if (leaseState == LEASE_ACTIVE || leaseState == LEASE_RENEGOTIATING) {

            return true;

        }

        return false;

    }
