            String s2 = v2.toString();

            int result = s1.compareTo(s2);

            if (result < 0) {

                return -1;

            } else if (result > 0) {

                return 1;

            } else {

                return 0;

            }

        }
