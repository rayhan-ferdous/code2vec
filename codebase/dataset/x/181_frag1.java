            date = new Date(time.longValue() * 1000);

        } catch (NumberFormatException ex) {

        }

        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern("EEE MMM d yyyy, hh:mm:ss a");

        return sdf.format(date);

    }



    public static Date hexDate(String hex) {

        try {

            Long time = Long.decode(hex);
