    public void test_1525_multiple_variant() {

        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {" + "  void m() {\n" + "    int a[] = {0,1,2,-3,4};\n" + "	 int i = 0, sum = 0, constant = 1;\n" + "    //@ decreases constant;\n" + "    //@ decreases i;\n" + "    //@ decreases sum;\n" + "	 for(;i != 4;) {\n" + "	 	sum += a[i++] + constant;\n" + "	 }\n" + "  }\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);

    }



    public void test_1526_multiple_variant() {

        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {" + "  void m() {\n" + "    int a[] = {0,1,2,-3,4};\n" + "	 int i = 0, sum = 0, constant = 1;\n" + "    //@ decreases constant;\n" + "    //@ decreases i;\n" + "    //@ decreases sum;\n" + "	 for(;i != 4;) {\n" + "	 	sum += a[i++] + constant++;\n" + "	 }\n" + "  }\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);

    }



    public void test_1527_multiple_variant() {

        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {" + "  void m() {\n" + "    int a[] = {0,1,2,-3,4};\n" + "	 int i = 0, sum = 0, constant = 1;\n" + "    //@ decreases constant;\n" + "    //@ decreases i;\n" + "    //@ decreases sum;\n" + "	 for(;i != 4;) {\n" + "	 	sum += a[i++] + constant++;\n" + "	 }\n" + "  }\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);

    }



    /** mixture of variants and invariants */

    public void test_1550_multiple_variant() {

        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {" + "  void m() {\n" + "    int a[] = {0,1,2,-3,4};\n" + "	 int i = 0, sum = 0, constant = 1;\n" + "    //@ maintaining sum >= 0;\n" + "    //@ decreases constant;\n" + "    //@ decreases i;\n" + "	 for(;i != 4;) {\n" + "	 	sum += a[i++] + constant++;\n" + "	 }\n" + "  }\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);

    }



    public void test_1551_multiple_variant() {
