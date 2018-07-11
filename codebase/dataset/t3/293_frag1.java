    private String getTaskSelect(String selected) {

        String selectedTask = store.getProperty(selected);

        StringBuffer buff = new StringBuffer(1024);

        HashMap<String, TaskCommand> tasks = store.getTaskList();

        String[] keys = (String[]) tasks.keySet().toArray(new String[0]);

        Arrays.sort(keys);

        buff.append("<select name='" + selected + "'>\n");

        if (selectedTask.length() == 0) buff.append("<option value='' selected>none</option>\n"); else buff.append("<option value='' >none</option>\n");

        for (int x = 0; x < keys.length; x++) {

            if (selectedTask.equalsIgnoreCase(keys[x])) buff.append("<option value='" + keys[x] + "' selected>" + keys[x] + "</option>\n"); else buff.append("<option value='" + keys[x] + "'>" + keys[x] + "</option>\n");

        }

        buff.append("</select>\n");

        return buff.toString();

    }
