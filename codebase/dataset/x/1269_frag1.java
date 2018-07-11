    public void schedualUpdateJob(LinkedList<String> fileId, String time, String period, String path) {

        if (jobSchedule == null) {

            jobSchedule = new JobScheduler();

        }

        String name = FOLDER_SCHEDULE + "sc" + String.valueOf(Math.random());

        jobSchedule.executeScheduleUpdate(fileId, time, name, period, path);

        scheduledJobs.addUpdateJob(jobSchedule.createName(name).substring(10), fileId, time, period, path);

        if (taskManager != null) {

            taskManager = new TaskManager(this, jobSchedule.createName(name));

            taskManager.run();

        }

    }
