    public void handle_command(Service who, String user, String replyto, String arguments) {

        String whatchan = "";

        String whom = "";

        String args[] = arguments.split(" ");

        boolean silent = false;

        whatchan = replyto;

        whom = user;

        if (args.length > 0 && (!(args[0].equals("")))) {

            if (args[0].startsWith("#")) {

                whatchan = args[0];
