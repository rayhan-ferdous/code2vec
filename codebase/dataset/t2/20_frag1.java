            int start = 0;

            int i = name.indexOf(oldT, start);

            while (i >= 0) {

                sb.append(name.substring(start, i));

                sb.append(newT);

                start = i + len;

                i = name.indexOf(oldT, start);

            }
