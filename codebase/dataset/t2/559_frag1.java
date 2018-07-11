            StringBuffer buf = new StringBuffer(line2.length);

            buf.append(line2, 0, i).append(newString2);

            i += oLength;

            int j = i;

            while ((i = line.indexOf(oldString, i)) > 0) {

                buf.append(line2, j, i - j).append(newString2);

                i += oLength;

                j = i;

            }

            buf.append(line2, j, line2.length - j);

            return buf.toString();
