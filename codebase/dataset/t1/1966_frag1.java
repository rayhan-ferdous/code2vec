    private static void exit(String prompt, boolean error) {

        if (prompt != null) {

            System.err.println(prompt);

        }

        if (error) {

            System.err.println(messages.getString("try"));

        }

        System.exit(1);

    }
