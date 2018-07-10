    private String getTaskList(String current) {

        StringBuffer buff = new StringBuffer(1024);

        String selectedTask = store.getProperty("tasks.deftask");

        if (current != null) {

            selectedTask = current;

        }

        HashMap<String, TaskCommand> tasks = store.getTaskList();

        String[] keys = (String[]) tasks.keySet().toArray(new String[0]);

        Arrays.sort(keys);

        buff.append("<select name='task'>\n");

        if (selectedTask.length() == 0) buff.append("<option value='none' selected>none</option>\n"); else buff.append("<option value='none'>none</option>\n");

        for (int x = 0; x < keys.length; x++) {

            if (keys[x].equals(selectedTask)) buff.append("<option value='" + keys[x] + "' selected>" + keys[x] + "</option>\n"); else buff.append("<option value='" + keys[x] + "'>" + keys[x] + "</option>\n");

        }

        buff.append("</select>\n");

        return buff.toString();

    }
