    ArrayList new25() {

        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();

        PReference preferenceNode1;

        {

            TParam tparamNode2;

            tparamNode2 = (TParam) nodeArrayList1.get(0);

            preferenceNode1 = new AFParamReference(tparamNode2);

        }

        nodeList.add(preferenceNode1);

        return nodeList;

    }
