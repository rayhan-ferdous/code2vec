    public static void sysWrite(String s1, int i, String s2) throws VM_PragmaNoInline {

        swLock();

        write(s1);

        write(i);

        write(s2);

        swUnlock();

    }
