            if (!a.hasMoreTokens()) {

                System.out.println("Invalid input file one line doesn't have " + "2 tokens");

                return null;

            }

            value = (new Integer(a.nextToken())).intValue();

            if (value > 1) break;

            narr.println(nextLine);
