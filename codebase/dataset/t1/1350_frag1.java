    DcmSnd(Configuration cfg, DcmURL url, int argc) {

        this.url = url;

        this.priority = Integer.parseInt(cfg.getProperty("prior", "0"));

        this.packPDVs = "true".equalsIgnoreCase(cfg.getProperty("pack-pdvs", "false"));

        this.truncPostPixelData = "true".equalsIgnoreCase(cfg.getProperty("trunc-post-pixeldata", "false"));

        this.excludePrivate = "true".equalsIgnoreCase(cfg.getProperty("exclude-private", "false"));

        this.bufferSize = Integer.parseInt(cfg.getProperty("buf-len", "2048")) & 0xfffffffe;

        this.repeatWhole = Integer.parseInt(cfg.getProperty("repeat-assoc", "1"));

        this.repeatSingle = Integer.parseInt(cfg.getProperty("repeat-dimse", "1"));

        this.studyUIDSuffix = cfg.getProperty("study-uid-suffix");

        this.seriesUIDSuffix = cfg.getProperty("series-uid-suffix");

        this.instUIDSuffix = cfg.getProperty("inst-uid-suffix");

        this.mode = argc > 1 ? SEND : initPollDirSrv(cfg) ? POLL : ECHO;

        initAssocParam(cfg, url, mode == ECHO);

        initTLS(cfg);

        initOverwrite(cfg);

    }
