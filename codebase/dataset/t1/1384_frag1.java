    public void doRegisterWrite(String command, String[] args) {

        if (args.length == 2) {

            try {

                int regnum = Integer.parseInt(args[0]);

                int data = parseHex32(args[1]);

                user.reg.write(regnum, data);

            } catch (NumberFormatException e) {

                jdp_console.writeOutput("bad value for write: " + args[0] + ", " + args[1]);

            }

        } else {

            printHelp(command);

        }

    }
