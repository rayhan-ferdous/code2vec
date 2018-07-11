                case '?':

                    exit(null, true);

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmGen DcmGen = new DcmGen(cfg, new DcmURL(args[optind]), argc);
