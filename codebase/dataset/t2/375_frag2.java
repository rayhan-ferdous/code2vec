        if (!filterOverride && !modsOverride) super.onTopic(channel, topic, setBy, date, changed);

        for (int i = 0; i < objFilters.length; i++) {

            if (objFilters[i].status) {

                ircData data = objFilters[i].onTopic(channel, topic, setBy, date, changed);

                channel = data.channel;

                topic = data.topic;

                setBy = data.setBy;

                date = data.date;

                changed = data.changed;

            }

        }

        if (filterOverride && !modsOverride) super.onTopic(channel, topic, setBy, date, changed);

        for (int i = 0; i < objMods.length; i++) {
