    public void testMethodInvocation9() {

        String cuText = "class Test {" + "	int number;" + "	void nostaticset(){" + "		number = 3;" + "	}" + "	static void testnostatic(){" + "		nostaticset();//error!\n" + "	}" + "}";

        CompilationUnit cu = null;

        try {

            cu = runIt(cuText).get(0);

        } catch (ParserException e) {

            fail(e.getMessage());

        }

        try {

            new SemanticsChecker(sc).check(cu);

            fail();

        } catch (Exception e) {

        }

    }
