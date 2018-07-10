    public static String getTarget(IRCMessage msg) {

        try {

            return (String) msg.getArgs().elementAt(1);

        } catch (ArrayIndexOutOfBoundsException e) {

            return "";

        }

    }
