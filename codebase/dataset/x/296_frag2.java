        } else if ("35".equals(urlData.getParameter("action"))) {

            outStream.write(remEpgWatchList(urlData));

            return;

        } else if ("36".equals(urlData.getParameter("action"))) {

            outStream.write(showEpgWatchListReport(urlData));

            return;

        } else if ("37".equals(urlData.getParameter("action"))) {

            outStream.write(showEpgWatchListMatches(urlData));

            return;

        } else if ("38".equals(urlData.getParameter("action"))) {

            outStream.write(addExcludeToMatchItem(urlData));

            return;

        } else if ("39".equals(urlData.getParameter("action"))) {

            outStream.write(showEditTextPage(urlData));

            return;

        } else if ("40".equals(urlData.getParameter("action"))) {

            outStream.write(updateTextPage(urlData));

            return;

        } else {
