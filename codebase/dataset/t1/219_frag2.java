        String cuText = "class TestStatic {" + "	static int i = 100;" + "	int a;" + "	static void set() {" + "		int j = i; //correct \n" + "	}" + "}";

        CompilationUnit cu = null;

        try {

            cu = runIt(cuText).get(0);

        } catch (ParserException e) {

            fail(e.getMessage());

        }

        try {

            new SemanticsChecker(sc).check(cu);
