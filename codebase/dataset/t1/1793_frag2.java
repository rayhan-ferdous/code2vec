    private String normalize(String name) {

        for (Pattern nameEquivalence : Diff.this.equivalentNames) {

            Matcher matcher = nameEquivalence.matcher(name.substring(1));

            if (matcher.matches()) {

                name = name.substring(0, 1);

                for (int i = 1; i <= matcher.groupCount(); i++) {

                    name += matcher.group(i);

                }

            }

        }

        return name;

    }
