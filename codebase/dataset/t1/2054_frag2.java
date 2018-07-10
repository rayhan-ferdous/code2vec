    private static void set(Configuration cfg, String s) {

        int pos = s.indexOf(':');

        if (!Character.isDigit(s.charAt(4))) {

            if (pos == -1) cfg.put("set.0." + s, ""); else cfg.put("set.0." + s.substring(0, pos), s.substring(pos + 1));

        } else {

            if (pos == -1) cfg.put("set." + s, ""); else cfg.put("set." + s.substring(0, pos), s.substring(pos + 1));

        }

    }
