        if (!filterOverride && !modsOverride) super.onSetChannelLimit(channel, sourceNick, sourceLogin, sourceHostname, limit);

        for (int i = 0; i < objFilters.length; i++) {

            if (objFilters[i].status) {

                ircData data = objFilters[i].onSetChannelLimit(channel, sourceNick, sourceLogin, sourceHostname, limit);

                channel = data.channel;

                sourceNick = data.sourceNick;

                sourceLogin = data.sourceLogin;

                sourceHostname = data.sourceHostname;

                limit = data.limit;

            }

        }

        if (filterOverride && !modsOverride) super.onSetChannelLimit(channel, sourceNick, sourceLogin, sourceHostname, limit);

        for (int i = 0; i < objMods.length; i++) {
