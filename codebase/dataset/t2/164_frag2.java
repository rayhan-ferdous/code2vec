    public void testMethodInvocation23() {

        String cuText = "class Hello{" + "	void say(){}" + "	class H{" + "		void say(){}" + "	}" + "	public static void main(){" + "		H.this.say();//error! \n" + "	}" + "}";

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
