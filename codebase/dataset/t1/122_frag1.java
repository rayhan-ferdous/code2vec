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

            String s1 = v1.toString();

            Object v2 = data.getValueAt(row2, column);

            String s2 = v2.toString();

            int result = s1.compareTo(s2);
