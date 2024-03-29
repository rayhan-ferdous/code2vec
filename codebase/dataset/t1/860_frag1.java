    public void doCurrentFrame(String command, String[] args) {

        try {

            int width, fp;

            switch(args.length) {

                case 0:

                    jdp_console.writeOutput(user.mem.printJVMstack(0, 4));

                    break;

                case 1:

                    if (args[0].startsWith("0x") || args[0].startsWith("0X") || args[0].length() == 8) {

                        fp = parseHex32(args[0]);

                        jdp_console.writeOutput(user.mem.printJVMstack(fp, 4));

                    } else {

                        width = Integer.parseInt(args[0]);

                        jdp_console.writeOutput(user.mem.printJVMstack(0, width));

                    }

                    break;

                case 2:

                    fp = parseHex32(args[0]);

                    width = Integer.parseInt(args[1]);

                    jdp_console.writeOutput(user.mem.printJVMstack(fp, width));

                    break;

            }

        } catch (NumberFormatException e) {

            printHelp(command);

        }

    }
