    private static Element createElement_pos(int depth, String title, Point pos, Document doc) {

        Element elem = doc.createElement(title);

        createCRLF_TAB(depth, elem, doc);

        Element elemSub = doc.createElement("posX");

        elemSub.appendChild(doc.createTextNode(String.valueOf(pos.x)));

        elem.appendChild(elemSub);

        createCRLF_TAB(depth, elem, doc);

        elemSub = doc.createElement("posY");

        elemSub.appendChild(doc.createTextNode(String.valueOf(pos.y)));

        elem.appendChild(elemSub);

        createCRLF_TAB(depth - 1, elem, doc);

        return elem;

    }
