    public static int iello(String say_this, int return_this) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return return_this;

    }
