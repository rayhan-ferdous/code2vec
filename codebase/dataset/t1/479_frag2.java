            System.exit(-1);

        } else {

            System.out.println("================= READY TO CALL: " + jello);

        }

        n_calls = 3;

        while (n_calls-- > 0) {

            Object jello_args[] = { new Integer(99), "I Say Jello to You!", new Integer(95), new Integer(94) };

            Integer result = (Integer) jello.invoke(null, jello_args);

            System.out.println("Does this>" + result + "< look like 99?");

        }

        tClass tc = new tClass("Hi!");

        String initargs[] = { "I'm dynamic!" };

        tClass tc_dyn = (tClass) ctors[0].newInstance(initargs);

        if (vello == null) {

            System.out.println("tClass.vello not found!");

            System.exit(-1);
