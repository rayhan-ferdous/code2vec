        } else if ("14".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(updateDeleteAfter(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("15".equals(urlData.getParameter("action"))) {
