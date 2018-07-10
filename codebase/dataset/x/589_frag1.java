    protected void runControllerExecutionFinishedBlock(ActionContext ac, Controller c, Ontology o) throws ExecutionException {

        if (actionblocks == null) {

            return;

        }

        prepareControllerEventBlocksAction(ac, c, o);

        try {

            actionblocks.controllerExecutionFinished();

        } catch (Throwable e) {

            if (e instanceof Error) {

                throw (Error) e;

            }

            if (e instanceof RuntimeException) {

                throw (RuntimeException) e;

            }

            throw new ExecutionException("Couldn't run controller finished action", e);

        }

    }
