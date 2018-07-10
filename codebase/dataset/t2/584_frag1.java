    public final void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {

        if (!filterOverride && !modsOverride) super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);

        for (int i = 0; i < objFilters.length; i++) {

            if (objFilters[i].status) {

                String str[] = objFilters[i].onQuit(sourceNick, sourceLogin, sourceHostname, reason);

                sourceNick = str[0];

                sourceLogin = str[1];

                sourceHostname = str[2];

                reason = str[3];

            }

        }

        if (filterOverride && !modsOverride) super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);

        for (int i = 0; i < objMods.length; i++) {

            if (objMods[i].status) objMods[i].onQuit(sourceNick, sourceLogin, sourceHostname, reason);

        }

    }
