        } else if (msg instanceof XLJ_L3PCC_Message) {

            XLJ_L3PCC_Message ccm = (XLJ_L3PCC_Message) msg;

            logger.finest("ISDN Call Control Message on Port:" + ccm.toString());

        } else if (msg instanceof XL_ChannelReleasedWithData) {

            XL_ChannelReleasedWithData crwd = (XL_ChannelReleasedWithData) msg;

            logger.finest("Channel Released With Data on Port:" + crwd.getSpan() + ":" + crwd.getChannel() + " - " + SKJMessage.printableFormat(crwd.getICBData()));
