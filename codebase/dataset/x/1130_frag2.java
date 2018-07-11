        Flags flags = m.getFlags();

        StringBuffer sb = new StringBuffer();

        Flags.Flag[] sf = flags.getSystemFlags();

        boolean first = true;

        for (int i = 0; i < sf.length; i++) {

            String s;

            Flags.Flag f = sf[i];

            if (f == Flags.Flag.ANSWERED) s = "\\Answered"; else if (f == Flags.Flag.DELETED) s = "\\Deleted"; else if (f == Flags.Flag.DRAFT) s = "\\Draft"; else if (f == Flags.Flag.FLAGGED) s = "\\Flagged"; else if (f == Flags.Flag.RECENT) s = "\\Recent"; else if (f == Flags.Flag.SEEN) s = "\\Seen"; else continue;

            if (first) first = false; else sb.append(' ');

            sb.append(s);

        }

        String[] uf = flags.getUserFlags();

        for (int i = 0; i < uf.length; i++) {

            if (first) first = false; else sb.append(' ');

            sb.append(uf[i]);

        }

        System.out.println("FLAGS = " + sb.toString());
