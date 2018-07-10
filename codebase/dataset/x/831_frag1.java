            return;

        }

        String name = part[1];

        if (name.equalsIgnoreCase(ALL)) {

            cp.hideAll();

        } else {

            Agent person;

            try {

                person = (Agent) control.getWorld().getPersonByName(name);

            } catch (AgentNotFoundException e) {

                sendError("Who's " + name + "?");
