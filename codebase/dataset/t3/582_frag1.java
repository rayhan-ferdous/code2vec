    private String getCaptureTypes(Document doc, Element formEl) {

        Element option = null;

        Text text = null;

        StringBuffer buff = new StringBuffer(1024);

        option = doc.createElement("option");

        option.setAttribute("value", "-1");

        text = doc.createTextNode("AutoSelect");

        option.appendChild(text);

        formEl.appendChild(option);

        Vector<CaptureCapability> capabilities = CaptureCapabilities.getInstance().getCapabilities();

        for (int x = 0; x < capabilities.size(); x++) {

            CaptureCapability capability = capabilities.get(x);

            option = doc.createElement("option");

            option.setAttribute("value", new Integer(capability.getTypeID()).toString());

            text = doc.createTextNode(capability.getName());

            option.appendChild(text);

            formEl.appendChild(option);

        }

        return buff.toString();

    }
