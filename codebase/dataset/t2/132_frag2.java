        item = doc.createElement("time");

        int hour = start.get(Calendar.HOUR_OF_DAY);

        if (hour > 12) hour = hour - 12; else if (hour == 0) hour = 12;

        item.setAttribute("hour_12", intToXchar(hour, 2));

        item.setAttribute("hour_24", intToXchar(start.get(Calendar.HOUR_OF_DAY), 2));

        item.setAttribute("minute", intToXchar(start.get(Calendar.MINUTE), 2));

        if (start.get(Calendar.AM_PM) == Calendar.AM) item.setAttribute("am_pm", "am"); else item.setAttribute("am_pm", "pm");

        root.appendChild(item);

        ScheduleItem[] itemsArray = store.getScheduleArray();
