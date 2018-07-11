    public AppiaMembership(View e) {

        addresses = new ArrayList<SocketAddress>(e.vs.addresses.length);

        for (int i = 0; i < e.vs.addresses.length; i++) {

            e.vs.addresses[i].toString();

            addresses.add(e.vs.addresses[i]);

        }

        myRank = e.ls.my_rank;

        coordinator = e.ls.coord;

        isBlocked = false;

        id = new AppiaMembershipID(e.vs.id.ltime, e.vs.id.coord);

        failed = e.ls.failed;

    }
