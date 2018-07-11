    private void assertOperationFails(String message, Operation operation) {

        try {

            operation.execute();

            fail(message);

        } catch (IllegalStateException ise) {

        }

    }
