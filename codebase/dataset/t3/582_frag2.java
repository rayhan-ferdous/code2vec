    private String getTaskList(Document doc, Element formEl) {

        Element option = null;

        Text text = null;

        StringBuffer buff = new StringBuffer(1024);

        HashMap<String, TaskCommand> tasks = store.getTaskList();

        String[] keys = (String[]) tasks.keySet().toArray(new String[0]);

        Arrays.sort(keys);

        option = doc.createElement("option");

        text = doc.createTextNode("none");

        option.appendChild(text);

        formEl.appendChild(option);

        for (int x = 0; x < keys.length; x++) {

            option = doc.createElement("option");

            text = doc.createTextNode(keys[x]);

            option.appendChild(text);

            formEl.appendChild(option);

        }

        return buff.toString();

    }
