    private boolean initPollDirSrv(Configuration cfg) {

        String pollDirName = cfg.getProperty("poll-dir", "", "<none>", "");

        if (pollDirName.length() == 0) {

            return false;

        }

        pollDir = new File(pollDirName);

        if (!pollDir.isDirectory()) {

            throw new IllegalArgumentException("Not a directory - " + pollDirName);

        }

        pollPeriod = 1000L * Integer.parseInt(cfg.getProperty("poll-period", "5"));

        pollDirSrv = PollDirSrvFactory.getInstance().newPollDirSrv(this);

        pollDirSrv.setOpenRetryPeriod(1000L * Integer.parseInt(cfg.getProperty("poll-retry-open", "60")) * 1000L);

        pollDirSrv.setDeltaLastModified(1000L * Integer.parseInt(cfg.getProperty("poll-delta-last-modified", "3")));

        String doneDirName = cfg.getProperty("poll-done-dir", "", "<none>", "");

        if (doneDirName.length() != 0) {

            File doneDir = new File(doneDirName);

            if (!doneDir.isDirectory()) {

                throw new IllegalArgumentException("Not a directory - " + doneDirName);

            }

            pollDirSrv.setDoneDir(doneDir);

        }

        return true;

    }
