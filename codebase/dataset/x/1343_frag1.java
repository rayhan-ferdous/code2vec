    public void leaveServiceGroup(String groupName, String servName, String servGroupName) {

        if (struct != null) {

            Group wg = struct.getGroup(groupName);

            if (wg != null && wg.isServiceGroupInService(servGroupName, servName)) {

                JChannel channelwg = (JChannel) channels_WG.get(groupName);

                if (channelwg != null) {

                    Message msg = new Message(null);

                    GMHeader head = new GMHeader(GMHeader.SP_KO_Serv_Nick, servName, servGroupName, nick);

                    msg.putHeader("GM", head);

                    Event evt = new Event(Event.MSG, msg);

                    channelwg.down(evt);

                } else System.out.println("JChannel channelwg null");

            }

        }

    }
