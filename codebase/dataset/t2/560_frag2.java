                outStream.write(deleteSchedules(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("14".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(updateDeleteAfter(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("15".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                showInfoDownloadPage(urlData, outStream);

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("16".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                showInfoDownloadPageArchive(urlData, outStream);

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else {
