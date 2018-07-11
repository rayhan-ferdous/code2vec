            if (ch == '\\') {

                ret.append("\\\\");

            } else if (ch == '\n') {

                ret.append("\\line ");

            } else if (ch == '\t') {

                ret.append("\\tab ");

            } else if (ch > z) {
