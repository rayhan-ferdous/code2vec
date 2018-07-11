    private final int match(String a, int startOffset, int stopOffset, String b) {

        int i = startOffset;

        for (int j = 0; j < b.length(); j++) {

            if (i >= stopOffset) return j;

            if (a.charAt(i) != b.charAt(j)) return j;

            i++;

        }

        return -1;

    }
