    public boolean display(ArrayList al) {

        String templateName = (String) al.get(2);

        XSLTInputSource xis = null;

        if (cacheing) {

            xis = (XSLTInputSource) templates.get(templateName);

            if (xis == null) {

                xis = new XSLTInputSource(templatePath + templateName);

                templates.put(templateName, xis);

            }

        }

        out.print(transformer.transform(rootDoc, xis));

        return false;
