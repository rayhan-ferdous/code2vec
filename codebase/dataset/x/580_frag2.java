    private static String getRandomPassword() {

        int n = 5;

        String symbols = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        Random rand = new Random();

        String s = "";

        for (int j = 0; j < n; j++) {

            s += symbols.toCharArray()[rand.nextInt(symbols.length())];

        }

        return s;

    }
