        this.excludePrivate = "true".equalsIgnoreCase(cfg.getProperty("exclude-private", "false"));

        this.bufferSize = Integer.parseInt(cfg.getProperty("buf-len", "2048")) & 0xfffffffe;

        this.repeatWhole = Integer.parseInt(cfg.getProperty("repeat-assoc", "1"));

        this.repeatSingle = Integer.parseInt(cfg.getProperty("repeat-dimse", "1"));

        this.uidSuffix = cfg.getProperty("uid-suffix");
