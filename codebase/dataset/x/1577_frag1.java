    private JMenuItem getToggleLaunchingJobsMenuItem() {

        debug.print("");

        if (toggleLaunchingJobsMenuItem == null) {

            toggleLaunchingJobsMenuItem = new JCheckBoxMenuItem();

            toggleLaunchingJobsMenuItem.setText("Do not launch queued jobs");

            toggleLaunchingJobsMenuItem.addItemListener(new ItemListener() {



                public void itemStateChanged(ItemEvent e) {

                    AbstractButton button = (AbstractButton) e.getItem();

                    Boolean enabled = button.isSelected();

                    if (enabled) {

                        jobMonitor.NoNewJobs = true;

                        log.warn("Launching queued jobs disabled. Queued jobs will not be launched.");

                    } else {

                        jobMonitor.NoNewJobs = false;

                        log.warn("Launching queued jobs enabled. Resuming normal job processing.");

                    }

                }

            });

        }

        return toggleLaunchingJobsMenuItem;

    }
