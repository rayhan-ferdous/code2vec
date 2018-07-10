        } else if (type == java.util.Date.class) {

            Date d1 = (Date) data.getValueAt(row1, column);

            long n1 = d1.getTime();

            Date d2 = (Date) data.getValueAt(row2, column);

            long n2 = d2.getTime();

            if (n1 < n2) {
