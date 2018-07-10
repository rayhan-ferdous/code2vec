        } else {

            Object v1 = data.getValueAt(row1, column);

            String s1 = v1.toString();

            Object v2 = data.getValueAt(row2, column);

            String s2 = v2.toString();

            int result = s1.compareTo(s2);

            if (result < 0) {
