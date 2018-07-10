    public String nextToken() {

        skipDelimiters();

        if (curPos >= maxPos) throw new NoSuchElementException();

        int start = curPos;

        if (str.charAt(curPos) == '\"') {

            start++;

            curPos++;

            boolean quoted = false;

            while (quoted || str.charAt(curPos) != '\"') {

                quoted = !quoted && str.charAt(curPos) == '\\';

                curPos++;

                if (curPos >= maxPos) throw new UnterminatedStringException();

            }

            StringBuffer sb = new StringBuffer();

            String s = str.substring(start, curPos++);

            int st = 0;

            for (; ; ) {

                int bs = s.indexOf('\\', st);

                if (bs == -1) break;

                sb.append(s.substring(st, bs));

                sb.append(s.substring(bs + 1, bs + 2));

                st = bs + 2;

            }

            sb.append(s.substring(st));

            return sb.toString();

        }

        while (curPos < maxPos && delim.indexOf(str.charAt(curPos)) < 0) curPos++;

        return str.substring(start, curPos);

    }
