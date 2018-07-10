    Node new23() {

        TIdent node2 = (TIdent) pop();

        TTestcase node1 = (TTestcase) pop();

        ATestcasename node = new ATestcasename(node1, node2);

        return node;

    }



    Node new24() {

        TOclblock node2 = (TOclblock) pop();

        TOclexpression node1 = (TOclexpression) pop();

        AOclExpression node = new AOclExpression(node1, node2);

        return node;

    }



    Node new25() {
