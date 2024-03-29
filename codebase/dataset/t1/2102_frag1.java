    protected void computeDefaultTimeOffset() {

        Calendar calendar = new GregorianCalendar();

        long tz = calendar.get(Calendar.ZONE_OFFSET);

        long dt = calendar.get(Calendar.DST_OFFSET);

        logger.finer("Time Zone offset: " + (-((double) (tz / 1000)) / (60.0 * 60.0)));

        logger.finer("Daylight Savings Time offset (h): " + (-((double) (dt / 1000)) / (60.0 * 60.0)));

        timeOffset = -(double) ((tz + dt) / 1000);

        logger.finer("timeOffset: " + timeOffset);

    }
