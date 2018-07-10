        BufferedReader br = new BufferedReader(new InputStreamReader(fs));

        String line = null;

        while ((line = br.readLine()) != null) {

            StringTokenizer st = new StringTokenizer(line);

            if (!st.hasMoreTokens()) continue;

            String keyword = st.nextToken();

            if (!st.hasMoreTokens()) {

                System.out.println("Invalid line: " + line);

                continue;

            }

            if (keyword.charAt(0) == '#') continue;

            if (keyword.equals("primary")) addPrimaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("secondary")) addSecondaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("cache")) {
