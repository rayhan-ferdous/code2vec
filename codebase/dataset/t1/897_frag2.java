            } else if (args[i].equals("--server")) {

                if (args.length < i + 1 || args[i].indexOf(":") == -1) {

                    try {

                        String ip = args[i + 1].substring(0, args[i + 1].indexOf(":"));

                        Globals.setServerIP(ip);

                        int port = Integer.parseInt(args[i + 1].substring(args[i + 1].indexOf(":") + 1));

                        Globals.setServerPort(port);

                        i++;

                    } catch (NumberFormatException e) {

                        System.out.println("ERROR: invalid value for <PORT>, number expected");

                    } catch (java.lang.StringIndexOutOfBoundsException e) {

                        System.out.println("ERROR: invalid value for <PORT>, number expected");

                    }

                } else {

                    System.out.println("ERROR: --server expects data in the form of \"127.0.0.1:6667\"");

                    printHelp();

                }

            } else if (args[i].equals("--debug")) {

                Globals.enableDebug();

            } else if (args[i].equals("--help")) {

                printHelp();

            } else {

                printHelp();

            }

        }

    }



    private static void printHelp() {

        System.out.println("\nCoopnetClient " + Globals.CLIENT_VERSION + " usage:\n" + "    java -jar CoopnetClient.jar [--server <IP>:<PORT>] [--debug]\n" + "\n" + "    --safemode resets all settings\n" + "    --server   ip and port of the server to connect to\n" + "    --debug    print debug messages during operation\n" + "    --help     print this help and exit\n");
