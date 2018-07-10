        Agent person;

        try {

            person = (Agent) control.getWorld().getPersonByName(name);

        } catch (AgentNotFoundException e) {

            sendError("Who's " + name + "?");

            return;

        }

        Position pos = person.getPos();

        try {

            send(cp.findPlacesNear(pos, dist));
