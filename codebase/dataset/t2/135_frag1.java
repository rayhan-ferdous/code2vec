    private String getTypeTag(XMLElement output_element) {

        Vector elements = output_element.getChildren();

        String type = null;

        for (int i = 0; i < elements.size() && type == null; i++) {

            XMLElement element = (XMLElement) elements.elementAt(i);

            if (element.getType().equals("type")) {

                type = element.getCharData();

            }

        }

        return type;

    }



    private String getTypeValue(XMLElement output_element) {
