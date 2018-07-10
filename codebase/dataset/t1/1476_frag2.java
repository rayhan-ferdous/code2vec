import java.util.*;

import java.lang.reflect.*;



public class tClass {



    public static void gc() {

        System.gc();

    }



    public static String hello(String say_this) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return "And I Say Hello Back";

    }



    public static int iello(String say_this, int return_this) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return return_this;

    }



    public static long lello(String say_this, long return_this) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return return_this;

    }



    public static int jello(int return_this, String say_this, int junk, int more_junk) {

        System.out.println("before gc arg=" + say_this);

        gc();

        System.out.println("after  gc arg=" + say_this);

        return return_this;

    }



    public String vello(String say_this) {

        return "And I Say Vello Back";

    }



    public tClass(String s) {

        System.out.println("tClass constructor called with " + s);

    }



    private tClass() {

        System.out.println("tClass private constructor called!");

    }



    public static void main(String args[]) throws Exception {

        Class c = Class.forName("tClass");

        System.out.println(c);

        try {

            Class c_not_found = Class.forName("NotAClassSoThrowAnExceptionPlease");

        } catch (ClassNotFoundException e) {

            System.out.println(e);
