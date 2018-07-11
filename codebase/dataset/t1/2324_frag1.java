    private static void set(Configuration cfg, String s) {

        int pos = s.indexOf(':');

        if (pos == -1) {

            cfg.put("set." + s, "");

        } else {

            cfg.put("set." + s.substring(0, pos), s.substring(pos + 1));

        }

    }
