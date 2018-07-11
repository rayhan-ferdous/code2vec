    public String getMinute() {

        String sVal = get("val");

        if (sVal.compareTo("") == 0) return "-1";

        String sPattern = "T(\\d+|MO|MI|DT|MI|EV|AF|XX):(\\d+)(:|$)";

        Pattern pattern = Pattern.compile(sPattern);

        Matcher m = pattern.matcher(sVal);

        if (m.find()) {

            try {

                String sRes = m.group(2);

                return sRes;

            } catch (Exception e) {

            }

        }

        return "-1";

    }



    public int getSecond() {

        String sVal = get("val");

        if (sVal.compareTo("") == 0) return -1;

        String sPattern = "T(\\d+|MO|MI|DT|MI|EV|AF|XX):(\\d+|XX):(\\d+)$";

        Pattern pattern = Pattern.compile(sPattern);

        Matcher m = pattern.matcher(sVal);

        if (m.find()) {

            try {

                int iRes = Integer.parseInt(m.group(3));

                return iRes;

            } catch (Exception e) {

            }

        }

        return -1;

    }



    public static String extend_To_TwoNumbers(String sAttribVal) {
