    public final boolean isPeer() {

        switch(this.type) {

            case TYPE_UDP:

            case TYPE_MDP:

                return true;

            default:

                return false;

        }

    }
