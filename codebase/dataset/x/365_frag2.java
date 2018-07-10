        System.err.println("[options] are:");

        System.err.println("  -quiet               No trace output");

        System.err.println("  -headless            No GUI");

        System.err.println("  -output <filename>   Output trace to <filename>");

        System.err.println("  -auto [n]            Automatically update on thread starts/stops.");

        System.err.println("                          Optional: n=delay in ms, n>=100. Default: 1000");

        System.err.println("  -obj                 Also process object sync points");

        System.err.println("  -debug               Process compact sync points with debug information");

        System.err.println("  -methoddb <filename> Specify <filename> as method database");

        System.err.println("  -initsp              Process sync points during VM initialization");

        System.err.println("  -termsp              Process sync points during VM termination");

        System.err.println("  -D <dir>             Set current directory (\"user.dir\") for debug JVM");
