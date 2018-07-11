                    mergeCfg = g.getOptarg();

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

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

            if (mergeCfg != null) {

                mergeCfg(cfg, mergeCfg);

            }

            DcmSnd dcmsnd = new DcmSnd(cfg, new DcmURL(args[optind]), argc);

            dcmsnd.execute(args, optind + 1);

        } catch (Exception e) {
