    public String _spaces(int rNumSpaces) {

        int numSpaces = rNumSpaces;

        if (0 > numSpaces) {

            numSpaces *= -1;

        }

        StringBuffer spaces = new StringBuffer(numSpaces);

        for (int spaceI = 0; spaceI < numSpaces; spaceI++) {

            spaces.append(" ");

        }

        return spaces.toString();

    }
