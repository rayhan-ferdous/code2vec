        if (agentMapping != agentMappingDone) buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Agent to Theme Map data not found in settings file</td></tr>");

        if (tasks != tasksDone) buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Tasks data not found in settings file</td></tr>");

        if (systemProp != systemPropDone) buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Server Settings data not found in settings file</td></tr>");

        if (schedules != schedulesDone) buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Schedule data not found in settings file</td></tr>");

        if (authSettings != authSettingsDone) buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Authentication Settings not found in settings file, or there was an error saving the data.</td></tr>");

        template.replaceAll("$result", buff.toString());
