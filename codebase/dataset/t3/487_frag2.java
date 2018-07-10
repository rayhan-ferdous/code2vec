    public void doFullFrame(String command, String[] args) {

        int from, to;

        try {

            switch(args.length) {

                case 0:

                    jdp_console.writeOutput(user.mem.printJVMstackTraceFull(0, 20));

                    break;

                case 1:

                    if (args[0].length() == 8) {

                        int fp = parseHex32(args[0]);

                        jdp_console.writeOutput(user.mem.printJVMstackTraceFull(fp));

                    } else {

                        from = Integer.parseInt(args[0]);

                        jdp_console.writeOutput(user.mem.printJVMstackTraceFull(from, from));

                    }

                    break;

                case 2:

                    from = Integer.parseInt(args[0]);

                    to = Integer.parseInt(args[1]);

                    jdp_console.writeOutput(user.mem.printJVMstackTraceFull(from, to));

                    break;

                default:

                    printHelp(command);

            }

        } catch (NumberFormatException e) {

            jdp_console.writeOutput("bad stack frame numbers (decimal) or frame pointer value (hex)");

        }

    }
