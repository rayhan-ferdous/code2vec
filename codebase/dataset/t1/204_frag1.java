        String expected = "DATA SEGMENT\nz_2 DW 3\na_3 DW 0\nc_0 DW 0\nb_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_2\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP b_1\n" + "PUSH 1\nPUSH b_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP c_0\nPUSH b_1\nPUSH c_0\n" + "PUSH 5\nPOP AX\nPOP BX\nMUL BX\nPUSH AX\nPUSH z_2\nPUSH 9\nPOP AX\nPOP BX\n" + "DIV BX\nPUSH AX\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP AX\nPOP BX\n" + "SUB AX, BX\nPUSH AX\nPOP a_3\n" + endProg;

        String actual = "";

        Reader r = new StringReader(code);

        this.processor.proccess(r);

        Boolean status = this.processor.getStatus();

        if (status) actual = this.processor.getGeneratedCode();

        Assert.assertEquals(expected, actual);

    }
