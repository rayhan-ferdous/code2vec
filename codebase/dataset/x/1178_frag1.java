    ArrayList new2() {

        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();

        PArgItem pargitemNode1;

        {

            TVararg tvarargNode2;

            tvarargNode2 = (TVararg) nodeArrayList1.get(0);

            pargitemNode1 = new AVarargArgItem(tvarargNode2);

        }

        nodeList.add(pargitemNode1);

        return nodeList;

    }
