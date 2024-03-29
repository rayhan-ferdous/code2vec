    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("dcmsnd", args, "", LONG_OPTS);

        Configuration cfg = new Configuration(DcmSnd.class.getResource("dcmsnd.cfg"));

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
