                addFile(output, f, "", compression);

            }

        }

        FileUtils.close(output);

    }



    /**

     * The command line entry point for jlink.

     * @param args an array of arguments

     */

    public static void main(String[] args) {

        if (args.length < 2) {

            System.out.println("usage: jlink output input1 ... inputN");

            System.exit(1);

        }

        jlink linker = new jlink();
