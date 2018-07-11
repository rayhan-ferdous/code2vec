    public void getResponse(HTTPurl urlData, OutputStream outStream, HashMap<String, String> headers) throws Exception {

        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(getLastChangeString());

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {

            outStream.write(showServerProperties(urlData));

            return;

        } else if ("03".equals(urlData.getParameter("action"))) {

            outStream.write(setServerProperty(urlData));

            return;

        } else if ("04".equals(urlData.getParameter("action"))) {

            outStream.write(getTunerList(urlData));

            return;

        } else if ("06".equals(urlData.getParameter("action"))) {

            outStream.write(showAutoDelItems(urlData));

            return;

        } else if ("07".equals(urlData.getParameter("action"))) {

            outStream.write(remAutoDelItem(urlData));

            return;

        } else if ("08".equals(urlData.getParameter("action"))) {

            outStream.write(showTasks(urlData));

            return;

        } else if ("09".equals(urlData.getParameter("action"))) {

            outStream.write(addTask(urlData, headers));

            return;

        } else if ("10".equals(urlData.getParameter("action"))) {

            outStream.write(remTask(urlData, headers));

            return;

        } else if ("11".equals(urlData.getParameter("action"))) {

            outStream.write(showJavaEnviroment(urlData, headers));

            return;

        } else if ("12".equals(urlData.getParameter("action"))) {

            outStream.write(setEpgTask(urlData));

            return;

        } else if ("13".equals(urlData.getParameter("action"))) {

            outStream.write(addTunerToList(urlData));

            return;

        } else if ("14".equals(urlData.getParameter("action"))) {

            outStream.write(remTunerFromList(urlData));

            return;

        } else if ("15".equals(urlData.getParameter("action"))) {

            outStream.write(moveTunerUp(urlData));

            return;

        } else if ("16".equals(urlData.getParameter("action"))) {

            outStream.write(moveTunerDown(urlData));

            return;

        } else if ("17".equals(urlData.getParameter("action"))) {

            outStream.write(enableTask(urlData, headers));

            return;

        } else if ("18".equals(urlData.getParameter("action"))) {

            outStream.write(exportTaskList(urlData));

            return;

        } else if ("19".equals(urlData.getParameter("action"))) {

            outStream.write(showAvailableThemes(urlData, headers));

            return;

        } else if ("20".equals(urlData.getParameter("action"))) {

            outStream.write(applyThemes(urlData));

            return;

        } else if ("22".equals(urlData.getParameter("action"))) {

            outStream.write(editTaskPage(urlData));

            return;

        } else if ("23".equals(urlData.getParameter("action"))) {

            outStream.write(updateTask(urlData, headers));

            return;

        } else if ("25".equals(urlData.getParameter("action"))) {

            outStream.write(showTaskImportForm(urlData));

            return;

        } else if ("26".equals(urlData.getParameter("action"))) {

            outStream.write(importTaskListData(urlData, headers));

            return;

        } else if ("27".equals(urlData.getParameter("action"))) {

            outStream.write(showCapPathPage(urlData));

            return;

        } else if ("28".equals(urlData.getParameter("action"))) {

            outStream.write(deleteNamePattern(urlData));

            return;

        } else if ("29".equals(urlData.getParameter("action"))) {

            outStream.write(addNamePattern(urlData));

            return;

        } else if ("30".equals(urlData.getParameter("action"))) {

            outStream.write(moveNamePattern(urlData));

            return;

        } else if ("31".equals(urlData.getParameter("action"))) {

            outStream.write(showAvailablePaths(urlData));

            return;

        } else if ("32".equals(urlData.getParameter("action"))) {

            outStream.write(addCapturePath(urlData));

            return;

        } else if ("33".equals(urlData.getParameter("action"))) {

            outStream.write(deleteCapturePath(urlData));

            return;

        } else if ("34".equals(urlData.getParameter("action"))) {

            outStream.write(moveCapturePath(urlData));

            return;

        } else if ("35".equals(urlData.getParameter("action"))) {

            outStream.write(updatePathSettings(urlData));

            return;

        } else if ("36".equals(urlData.getParameter("action"))) {

            outStream.write(addAgentToThemeMap(urlData));

            return;

        } else if ("37".equals(urlData.getParameter("action"))) {

            outStream.write(remAgentToThemeMap(urlData));

            return;

        } else if ("38".equals(urlData.getParameter("action"))) {

            exportAllSettings(urlData, outStream);

            return;

        } else if ("39".equals(urlData.getParameter("action"))) {

            outStream.write(importAllSettings(urlData, headers));

            return;

        } else if ("40".equals(urlData.getParameter("action"))) {

            outStream.write(showRunningActions(urlData, headers));

            return;

        }

        outStream.write(showSystemInfo(urlData));

    }
