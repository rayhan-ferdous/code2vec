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



    void processTemplateRegion(Vector region) throws IOException {

        String inLine = (String) region.elementAt(0);

        int command = getTemplateCommand(inLine);

        switch(command) {

            case FOREACH:

                processForeachRegion(region);

                break;

            case LOOP:

                processLoopRegion(region);

                break;

            case COUNT:

                processCountRegion(region);

                break;

            case SPLIT:

                processSplitRegion(region);

                break;

            case JOIN:

                processJoinRegion(region);

                break;

            case LET:

                processLetRegion(region);

                break;

            case EVAL:

                processEvalRegion(region);

                break;

            case IF:

                processCondRegion(region);

                break;

            case INCLUDE:

                processIncludeRegion(region);

                break;

            default:

                throw new IOException("Invalid command");

        }

    }



    private String getNextLine(BufferedReader data) throws IOException {

        String line = data.readLine();

        if (line == null || line.length() == 0) return line;

        while (line.startsWith(commentMarker)) line = data.readLine();

        while (line.endsWith("\\")) line = line.substring(0, line.length() - 1) + data.readLine();

        return line;

    }



    void processForeachRegion(Vector region) throws IOException {

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing variable in FOREACH");

        String var_name = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing filename in FOREACH");

        String file_name = pst.nextToken();

        String select = null;

        String start = null;

        String end = null;

        boolean inRange = false;

        if (pst.hasMoreTokens()) {

            select = pst.nextToken();

            if (!pst.hasMoreTokens()) throw new IOException("Missing field value in FOREACH");

            String fval = pst.nextToken();

            int dotdot = fval.indexOf("..");

            if (dotdot != -1 && dotdot == fval.lastIndexOf("..")) {

                start = fval.substring(0, dotdot);

                end = fval.substring(dotdot + 2);

            } else {

                start = fval;

            }

        }

        if (DEBUG) System.out.println("doing foreach with varname " + var_name + " on data file :" + file_name);

        if (DEBUG && select != null) {

            System.out.print("   selecting records with " + select);

            if (end == null) System.out.println(" equal to \"" + start + "\""); else System.out.println(" between \"" + start + "\" and \"" + end + "\"");

        }

        BufferedReader data = new BufferedReader(new FileReader(file_name));
