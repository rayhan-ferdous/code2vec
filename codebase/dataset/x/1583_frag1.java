    void run() throws Exception {

        Set<String> found = new TreeSet<String>();

        ClassFile cf = getClassFile("T6887895$Test.class");

        for (CPInfo cpInfo : cf.constant_pool.entries()) {

            if (cpInfo instanceof CONSTANT_Class_info) {

                CONSTANT_Class_info info = (CONSTANT_Class_info) cpInfo;

                String name = info.getName();

                String baseName = info.getBaseName();

                System.out.println("found: " + name + " " + baseName);

                if (baseName != null) found.add(baseName);

            }

        }

        String[] expectNames = { "java/lang/Object", "java/lang/String", "T6887895", "T6887895$Test" };

        Set<String> expect = new TreeSet<String>(Arrays.asList(expectNames));

        if (!found.equals(expect)) {

            System.err.println("found: " + found);

            System.err.println("expect: " + expect);

            throw new Exception("unexpected values found");

        }

    }
