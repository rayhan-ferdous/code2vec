    public int countTokens() {

        int count = 0;

        int pos = curPos;

        while (pos < maxPos) {

            while (pos < maxPos && delim.indexOf(str.charAt(pos)) >= 0) pos++;

            if (pos >= maxPos) break;

            if (str.charAt(pos) == '\"') {

                int start = ++pos;

                boolean quoted = false;

                while (quoted || str.charAt(pos) != '\"') {

                    quoted = !quoted && str.charAt(pos) == '\\';

                    pos++;

                    if (pos >= maxPos) throw new UnterminatedStringException();

                }

                pos++;

            } else {

                int start = pos;

                while (pos < maxPos && delim.indexOf(str.charAt(pos)) < 0) pos++;

            }

            count++;

        }

        return count;

    }
