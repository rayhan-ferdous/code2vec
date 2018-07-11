    public Time toTime(String value) {

        if (value != null && value.length() > 0) {

            try {

                SimpleDateFormat sdf = new SimpleDateFormat(timePattern.substring(0, value.length()), locale);

                return new Time(sdf.parse(value).getTime());

            } catch (ParseException e) {

            }

        }

        return null;

    }
