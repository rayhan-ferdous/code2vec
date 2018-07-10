        TGChannel channel = this.factory.newChannel();

        TGChannelParameter gmChannel1Param = this.factory.newChannelParameter();

        TGChannelParameter gmChannel2Param = this.factory.newChannelParameter();

        int channel1 = (readByte() & 0xff);

        gmChannel1Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_1);

        gmChannel1Param.setValue(Integer.toString(channel1));

        int channel2 = (readByte() & 0xff);

        gmChannel2Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_2);

        gmChannel2Param.setValue(Integer.toString(channel2));

        channel.setBank(channel1 == 9 ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);

        channel.setProgram((short) readByte());
