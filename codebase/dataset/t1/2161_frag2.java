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
