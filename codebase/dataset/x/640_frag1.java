    public static void main(String[] argv) {

        try {

            int num = 1;

            if (argv.length > 0) {

                num = Integer.parseInt(argv[0]);

            }

            Thread[] threadList = new Thread[num];

            for (int i = 0; i < num; ++i) {

                threadList[i] = new TestRuntimeExec(i);

                threadList[i].start();

            }

            for (int i = 0; i < num; ++i) threadList[i].join();

            System.out.println("All test threads finished");

        } catch (Exception e) {

            System.out.println("TestRuntimeExec: FAILED");

            System.exit(1);

        }

    }
