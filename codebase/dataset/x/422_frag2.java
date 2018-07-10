            int x = lineEnd(true, pre);

            String l = UTF.equals(cs_) ? new String(String2.utf(bs, begin, x)) : new String(bs, begin, x - begin, cs_);

            begin = pre ? x + 2 : x + 1;
