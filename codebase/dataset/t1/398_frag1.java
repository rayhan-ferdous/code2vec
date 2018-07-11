    public static String hello(String say_this) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return "And I Say Hello Back";

    }
