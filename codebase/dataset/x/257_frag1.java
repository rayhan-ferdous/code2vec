    String substitute(String input, String var, String value) throws IOException {

        StringBuffer out = new StringBuffer();

        int varlen = var.length();

        int oidx = 0;

        for (; ; ) {

            int idx = input.indexOf(var, oidx);

            if (idx == -1) break;

            out.append(input.substring(oidx, idx));

            idx += varlen;

            out.append(value);

            oidx = idx;

        }

        out.append(input.substring(oidx));

        return out.toString();

    }
