        } else if (type == java.util.Date.class) {

            Date d1 = (Date) data.getValueAt(row1, column);

            long n1 = d1.getTime();

            Date d2 = (Date) data.getValueAt(row2, column);

            long n2 = d2.getTime();

            if (n1 < n2) {

                return -1;

            } else if (n1 > n2) {

                return 1;

            } else {

                return 0;

            }

        } else if (type == String.class) {

            String s1 = (String) data.getValueAt(row1, column);

            String s2 = (String) data.getValueAt(row2, column);

            int result = s1.compareTo(s2);

            if (result < 0) {

                return -1;

            } else if (result > 0) {

                return 1;

            } else {

                return 0;

            }

        } else if (type == Boolean.class) {

            Boolean bool1 = (Boolean) data.getValueAt(row1, column);

            boolean b1 = bool1.booleanValue();

            Boolean bool2 = (Boolean) data.getValueAt(row2, column);

            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {

                return 0;

            } else if (b1) {

                return 1;

            } else {

                return -1;

            }

        } else {

            Object v1 = data.getValueAt(row1, column);
