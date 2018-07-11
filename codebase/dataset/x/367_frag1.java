        if (D) Log.d(TAG, "Start!");

        mCallback = handler;

        mThread = new ClientThread(mContext, mTransport);

        mThread.start();

    }



    public void stop() {

        if (D) Log.d(TAG, "Stop!");

        if (mThread != null) {

            mInterrupted = true;
