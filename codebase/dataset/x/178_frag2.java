            } else if (commFlags.hasParam("/payload/resetLogfile")) {

                log.log(log.SERVICES, "Refreshing debug files");

                DebugLogger.refreshDebugFile();

            } else if (commFlags.hasParam("/payload/reportStatus")) {

                log.log(log.SERVICES, "Forcing a push of status");

            } else if (commFlags.hasParam("/payload/reportParameters")) {
