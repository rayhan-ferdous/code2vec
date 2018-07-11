    String substitute(String input, String var, String[] fields, String[] fieldData) throws IOException {

        StringBuffer out = new StringBuffer();

        int varlen = var.length();

        int oidx = 0;

        for (; ; ) {

            if (DEBUG) System.out.println("checking for occurrence of " + var + " in :" + input.substring(oidx));

            int idx = input.indexOf(var, oidx);

            if (idx == -1) break;

            out.append(input.substring(oidx, idx));

            idx += varlen;

            if (input.charAt(idx) != '.') throw new IOException("no field");

            idx++;

            int idx_save = idx;

            for (int i = 0; i < fields.length; i++) {

                String fld = fields[i];

                int flen = fld.length();

                if (DEBUG) System.out.println("checking if it is field " + fld);

                if (input.regionMatches(idx, fld, 0, flen)) {

                    String value = fieldData[i];

                    if (DEBUG) System.out.println("field matches. outputting data :" + value);

                    out.append(value);

                    idx += flen;

                    break;

                }

            }

            if (idx == idx_save) throw new IOException("unknown field");

            oidx = idx;

        }

        if (DEBUG) System.out.println("no more variables left on this line");

        out.append(input.substring(oidx));

        return out.toString();

    }
