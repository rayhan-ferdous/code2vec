                outStream.write(deleteSchedule(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("05".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(addTime(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("06".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(skipToNext(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("07".equals(urlData.getParameter("action"))) {

            ThreadLock.getInstance().getLock();

            try {

                outStream.write(showItemInfo(urlData));

            } finally {

                ThreadLock.getInstance().releaseLock();

            }

            return;

        } else if ("09".equals(urlData.getParameter("action"))) {
