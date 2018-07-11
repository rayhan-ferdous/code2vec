            int nstages = flowTable.getValues().size();

            for (int i = 0; i < nstages; i++) {

                try {

                    BoundaryInput input = new BoundaryInput();

                    input.name = flowTable.getValue(i, "NAME");

                    input.nodeId = flowTable.getValue(i, "NODE");

                    input.sign = Integer.parseInt(flowTable.getValue(i, "SIGN"));
