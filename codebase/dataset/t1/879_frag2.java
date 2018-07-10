    void processIncludeRegion(Vector region) throws IOException {

        for (int j = 1; j < region.size(); j++) {

            try {

                String result = (String) region.elementAt(j);

                out.print(result + "\n");

            } catch (ClassCastException e) {

                Vector newRegion = (Vector) region.elementAt(j);

                processTemplateRegion(newRegion);

            }

        }

    }
