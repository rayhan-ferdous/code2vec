        } else if ("04".equals(urlData.getParameter("action"))) {

            outStream.write(showChannelMapping(urlData));

            return;

        } else if ("05".equals(urlData.getParameter("action"))) {

            outStream.write(addChannelMapping(urlData));

            return;

        } else if ("06".equals(urlData.getParameter("action"))) {

            outStream.write(showProgramDetails(urlData));

            return;

        } else if ("07".equals(urlData.getParameter("action"))) {
