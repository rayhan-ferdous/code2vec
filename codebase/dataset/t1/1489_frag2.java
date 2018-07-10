    void processEvalRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("doing eval");

        PrintWriter old_out = out;

        StringWriter sw = new StringWriter();

        out = new PrintWriter(sw);

        for (int j = 1; j < region.size(); j++) {

            try {

                String currentLine = (String) region.elementAt(j);

                out.print(currentLine + "\n");

            } catch (ClassCastException e) {

                Vector tmpRegion = (Vector) region.elementAt(j);

                processTemplateRegion(tmpRegion);

            }

        }

        out = old_out;

        if (DEBUG) System.out.println("doing eval: evaluating\n" + sw);

        LineNumberReader old_in = in;

        in = new LineNumberReader(new StringReader(sw.toString()));

        String inLine;

        for (inLine = readLine(); inLine != null; inLine = readLine()) {

            if (DEBUG) System.out.println("from input:" + inLine);

            if (!isTemplateLine(inLine)) {

                if (DEBUG) System.out.println("not template line, continuing...");

                out.print(inLine + "\n");

                continue;

            }

            Vector newRegion = buildTemplateRegion(inLine);

            processTemplateRegion(newRegion);

        }

        in = old_in;

    }
