        return (ChanList) getSource();

    }



    public void channelBegin(ChanList list) {

        clear(getSource());

        print("Retrieving available rooms...");

        _list.setFirst(0);

    }



    private void sort(ChannelInfo[] info, int begin, int end, int deep) {

        if (deep < 50) {

            if (begin < end) {

                ChannelInfo tmp;

                int f = (begin + end) / 2;
