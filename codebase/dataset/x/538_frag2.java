        for (int i = 0; i < x1.length; i++) {

            x1[i] = i;

        }

        try {

            bcmWFMonitor = bcmWF.addMonitorValTime(new IEventSinkValTime() {



                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {

                    yBCMWF = newRecord.doubleArray();

                    bcmPlotData.setPoints(x1, yBCMWF);

                    bcmPlot.refreshGraphJPanel();

                }

            }, Monitor.VALUE);
