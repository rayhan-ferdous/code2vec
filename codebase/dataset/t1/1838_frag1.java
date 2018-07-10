        Configuration cfg = new Configuration(DcmGen.class.getResource("dcmgen.cfg"));

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 2:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), g.getOptarg());

                    break;

                case 'P':

                    cfg.put("prior", "1");

                    break;

                case 'p':

                    cfg.put("prior", "2");

                    break;

                case 'k':

                    cfg.put("pack-pdvs", "true");

                    break;

                case 't':

                    cfg.put("trunc-post-pixeldata", "true");

                    break;

                case 'x':

                    cfg.put("exclude-private", "true");

                    break;

                case 's':

                    set(cfg, g.getOptarg());

                    break;

                case 'r':
