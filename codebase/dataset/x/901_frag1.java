        public void getTree(final int pagenumber) {

            if (mLoadTreeThread != null) {

                try {

                    mLoadTreeThread.join();

                } catch (InterruptedException ie) {

                    ie.printStackTrace();

                }

                mLoadTreeThread = null;

            }

            mCurrentPage = pagenumber;

            mLoadTreeThread = new Thread(this);

            mLoadTreeThread.start();

        }
