    private boolean grep(File f, CharBuffer cb) {

        Matcher lm = linePattern.matcher(cb);

        Matcher pm = null;

        int lines = 0;

        while (lm.find()) {

            lines++;

            CharSequence cs = lm.group();

            if (pm == null) pm = pattern.matcher(cs); else pm.reset(cs);

            if (pm.find()) return true;

            if (lm.end() == cb.limit()) break;

        }

        return false;

    }
