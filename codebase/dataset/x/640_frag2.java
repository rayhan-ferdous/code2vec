    RBNB() {

        super();

        try {

            setPathDoor(new Door(Door.READ_WRITE));

            rClientsDoor = new Door(Door.READ_WRITE);

            routingDoor = new Door(Door.STANDARD);

        } catch (java.lang.Exception e) {

        }

        setServerSide(this);

    }
