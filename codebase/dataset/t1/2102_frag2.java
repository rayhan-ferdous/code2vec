    private void computeDefaultTimeOffset() {

        Calendar calendar = new GregorianCalendar();

        long tz = calendar.get(Calendar.ZONE_OFFSET);

        long dt = calendar.get(Calendar.DST_OFFSET);

        log.info("Default time: Time Zone offset: " + (-((double) (tz / 1000)) / (60.0 * 60.0)));

        log.info("Default time: Daylight Savings Time offset (in hours): " + (-((double) (dt / 1000)) / (60.0 * 60.0)));

        timeOffset = -(double) ((tz + dt) / 1000);

    }
