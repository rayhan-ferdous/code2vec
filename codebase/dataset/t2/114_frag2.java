            int nstages = sourceFlowTable.getValues().size();

            for (int i = 0; i < nstages; i++) {

                try {

                    BoundaryInput input = new BoundaryInput();

                    input.name = sourceFlowTable.getValue(i, "NAME");

                    input.nodeId = sourceFlowTable.getValue(i, "NODE");

                    input.sign = Integer.parseInt(sourceFlowTable.getValue(i, "SIGN"));
