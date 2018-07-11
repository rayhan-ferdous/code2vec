            LOGGER.debug("Sending Question Name: " + question);

            out.print(question + "\r\n");

            out.flush();

            String line;

            try {

                line = in.readLine();

            } catch (NoSuchElementException exc) {

                line = null;

            }

            if (line == null) done = true; else if (line.equals("INVALID COMMAND OR ASSIGNMENT")) {
