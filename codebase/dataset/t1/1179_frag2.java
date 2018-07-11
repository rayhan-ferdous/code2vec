    public String _getUserArg(int rOrdinal) {

        if (null == writer__iArgs) {

            return "";

        }

        int ordinal = 0;

        int numArgs = writer__iArgs.length;

        next_arg: for (int argI = 0; argI < numArgs; argI++) {

            if (writer__iArgs[argI].startsWith(writer__ARGUMENT_CONTROL_PREFIX)) {

                continue next_arg;

            } else {

                if (ordinal == rOrdinal) {

                    return writer__iArgs[argI];

                } else {

                    ordinal++;

                }

            }

        }

        return "";

    }
