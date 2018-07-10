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
