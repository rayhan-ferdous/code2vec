        if (pos == -1) {

            cfg.put("key." + s, "");

        } else {

            cfg.put("key." + s.substring(0, pos), s.substring(pos + 1));

        }

    }



    DcmDir(File dirfile, Properties cfg) {

        this.dirFile = dirfile;
