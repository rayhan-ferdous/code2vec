    public static String getCurrentDateTime() {

        String week = new String("日一二三四五六");

        Date currentDate = new Date();

        String str = "";

        str = (2000 + currentDate.getYear() % 100) + "年 ";

        str = str + currentDate.getMonth() + "月";

        str = str + currentDate.getDate() + "日 ";

        int day = currentDate.getDay();

        str = str + "星期" + week.substring(day, day + 1) + " ";

        str = str + currentDate.getHours() + "点 ";

        str = str + currentDate.getMinutes() + "分 ";

        str = str + currentDate.getSeconds() + "秒 ";

        return str;

    }
