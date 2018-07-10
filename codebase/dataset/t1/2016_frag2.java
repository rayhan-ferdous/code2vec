        } else if ("04".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(rescanXMLTVdata(urlData, headers));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("05".equals(urlData.getParameter("action"))) {
