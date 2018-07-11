    public static String[] safeGetParameters(String parameter, HttpServletRequest request) {

        String[] values = request.getParameterValues(parameter);

        if (values == null) {

            return new String[0];

        }

        for (int i = 0; i < values.length; i++) {

            if (values[i] == null) {

                values[i] = "";

            }

            values[i] = superStrip(values[i]);

        }

        return values;

    }
