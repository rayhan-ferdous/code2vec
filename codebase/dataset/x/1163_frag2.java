        c = (char) ((x >> 4) & 0xf);

        if (c > 9) {

            c = (char) ((c - 10) + 'a');

        } else {

            c = (char) (c + '0');

        }

        sb.append(c);

        c = (char) (x & 0xf);

        if (c > 9) {

            c = (char) ((c - 10) + 'a');

        } else {

            c = (char) (c + '0');

        }
