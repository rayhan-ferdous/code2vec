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
