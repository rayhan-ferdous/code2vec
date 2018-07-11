    public void doMemoryWrite(String command, String[] args) {

        if (args.length == 2) {

            try {

                int data = parseHex32(args[1]);

                int addr = parseHex32(args[0]);

                user.mem.write(addr, data);

            } catch (NumberFormatException e) {

                jdp_console.writeOutput("bad value for write: " + args[0] + ", " + args[1]);

            }

        } else {

            printHelp(command);

        }

    }
