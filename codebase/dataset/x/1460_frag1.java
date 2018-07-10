    public Channel joinChannel(final String channelName) {

        if (channels.containsKey(channelName.trim())) {

            final Channel channel = channels.get(channelName);

            if (!channel.isRunning) {

                channel.join();

            }

            return channel;

        }

        final Channel channel = new Channel(channelName, this);

        channel.join();

        return channel;

    }
