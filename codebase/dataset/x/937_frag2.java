    public static String extractArguments(String pArg, String pVarName) {

        String result = null;

        int argPos = -1;

        argPos = pArg.indexOf(pVarName);

        if (argPos != -1) {

            String fields = pArg.substring(pVarName.length());

            if (fields.length() > 0) {

                result = fields;

            }

        }

        return (result);

    }
