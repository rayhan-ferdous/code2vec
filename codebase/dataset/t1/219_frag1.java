        String cuText = "class Addtion {" + "	static int get(){return get(1);}" + "	private static int get(int i) { return 2*i; }" + "   public static void main(){" + "		this.get();" + "	}" + "}";

        CompilationUnit cu = null;

        try {

            cu = runIt(cuText).get(0);

        } catch (ParserException e) {

            fail(e.getMessage());

        }

        try {

            new SemanticsChecker(sc).check(cu);
