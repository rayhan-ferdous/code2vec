    public List<String> grep(String input, String pattern, boolean caseInsensitive) {

        ArrayList<String> ret = new ArrayList<String>();

        Scanner scn = new Scanner(input);

        int Options = 0;

        if (caseInsensitive) {

            Options |= Pattern.CASE_INSENSITIVE;

        }

        Pattern patRegex = Pattern.compile(pattern, Options);

        while (scn.hasNext()) {

            String curr = scn.nextLine();

            if (patRegex.matcher(curr).matches()) {

                ret.add(curr);

            }

        }

        return ret;

    }
