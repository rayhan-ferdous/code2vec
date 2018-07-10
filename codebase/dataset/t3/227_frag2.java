    public int compareTo(ScheduleItem comTo) {

        if (comTo == null) return -1;

        long comStart = comTo.getStart().getTime();

        long thisStart = getStart().getTime();

        int result = 0;

        if (comStart > thisStart) result = -1; else if (comStart < thisStart) result = +1;

        return result;

    }
