    int getTemplateCommand(String line) {

        int startMatch = line.indexOf(templateMarker) + templateMarker.length() + 1;

        if (DEBUG) System.out.println("getting template command :" + line.substring(startMatch));

        for (int i = 0; i < commands.length; ++i) {

            String current = commands[i];

            if (line.regionMatches(startMatch, current, 0, current.length())) {

                params = line.substring(startMatch + current.length());

                if (DEBUG) System.out.println("command is " + commands[i] + ". params =" + params);

                return i;

            }

        }

        return INVALID_COMMAND;

    }
