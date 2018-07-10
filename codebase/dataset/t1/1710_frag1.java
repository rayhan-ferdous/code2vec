    public static String toString(String notifyDataLine) {

        String[] fields = decodeReportData(notifyDataLine);

        if (fields == null) {

            return null;

        }

        StringBuffer buf = new StringBuffer();

        buf.append("ANTLR Runtime Report; Profile Version ");

        buf.append(fields[0]);

        buf.append('\n');

        buf.append("parser name ");

        buf.append(fields[1]);

        buf.append('\n');

        buf.append("Number of rule invocations ");

        buf.append(fields[2]);

        buf.append('\n');

        buf.append("Number of rule invocations in \"guessing\" mode ");

        buf.append(fields[27]);

        buf.append('\n');

        buf.append("max rule invocation nesting depth ");

        buf.append(fields[3]);

        buf.append('\n');

        buf.append("number of fixed lookahead decisions ");

        buf.append(fields[4]);

        buf.append('\n');

        buf.append("min lookahead used in a fixed lookahead decision ");

        buf.append(fields[5]);

        buf.append('\n');

        buf.append("max lookahead used in a fixed lookahead decision ");

        buf.append(fields[6]);

        buf.append('\n');

        buf.append("average lookahead depth used in fixed lookahead decisions ");

        buf.append(fields[7]);

        buf.append('\n');

        buf.append("standard deviation of depth used in fixed lookahead decisions ");

        buf.append(fields[8]);

        buf.append('\n');

        buf.append("number of arbitrary lookahead decisions ");

        buf.append(fields[9]);

        buf.append('\n');

        buf.append("min lookahead used in an arbitrary lookahead decision ");

        buf.append(fields[10]);

        buf.append('\n');

        buf.append("max lookahead used in an arbitrary lookahead decision ");

        buf.append(fields[11]);

        buf.append('\n');

        buf.append("average lookahead depth used in arbitrary lookahead decisions ");

        buf.append(fields[12]);

        buf.append('\n');

        buf.append("standard deviation of depth used in arbitrary lookahead decisions ");

        buf.append(fields[13]);

        buf.append('\n');

        buf.append("number of evaluated syntactic predicates ");

        buf.append(fields[14]);

        buf.append('\n');

        buf.append("min lookahead used in a syntactic predicate ");

        buf.append(fields[15]);

        buf.append('\n');

        buf.append("max lookahead used in a syntactic predicate ");

        buf.append(fields[16]);

        buf.append('\n');

        buf.append("average lookahead depth used in syntactic predicates ");

        buf.append(fields[17]);

        buf.append('\n');

        buf.append("standard deviation of depth used in syntactic predicates ");

        buf.append(fields[18]);

        buf.append('\n');

        buf.append("rule memoization cache size ");

        buf.append(fields[28]);

        buf.append('\n');

        buf.append("number of rule memoization cache hits ");

        buf.append(fields[25]);

        buf.append('\n');

        buf.append("number of rule memoization cache misses ");

        buf.append(fields[26]);

        buf.append('\n');

        buf.append("number of evaluated semantic predicates ");

        buf.append(fields[19]);

        buf.append('\n');

        buf.append("number of tokens ");

        buf.append(fields[20]);

        buf.append('\n');

        buf.append("number of hidden tokens ");

        buf.append(fields[21]);

        buf.append('\n');

        buf.append("number of char ");

        buf.append(fields[22]);

        buf.append('\n');

        buf.append("number of hidden char ");

        buf.append(fields[23]);

        buf.append('\n');

        buf.append("number of syntax errors ");

        buf.append(fields[24]);

        buf.append('\n');

        return buf.toString();

    }
